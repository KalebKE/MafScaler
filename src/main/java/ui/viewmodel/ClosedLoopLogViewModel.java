package ui.viewmodel;

import io.reactivex.subjects.PublishSubject;
import parser.me7log.Me7LogParser;
import parser.mlhfm.MlhfmParser;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ClosedLoopLogViewModel {

    private Me7LogParser me7LogParser;
    private PublishSubject<Map<String, List<Double>>> publishSubject;

    private static ClosedLoopLogViewModel instance;

    public static ClosedLoopLogViewModel getInstance() {
        if(instance == null) {
            instance = new ClosedLoopLogViewModel();
        }

        return instance;
    }

    private ClosedLoopLogViewModel() {
        me7LogParser = new Me7LogParser();
        publishSubject = PublishSubject.create();
    }

    public PublishSubject<Map<String, List<Double>>> getPublishSubject() {
        return publishSubject;
    }

    public void loadFile(File directory) {
        if(directory.isDirectory()) {
            CompletableFuture.runAsync(() -> {
                Map<String, List<Double>> me7LogMap = me7LogParser.parseLogFile(Me7LogParser.LogType.CLOSED_LOOP, directory);

                if (me7LogMap != null) {
                    publishSubject.onNext(me7LogMap);
                }
            });
        } else {
            publishSubject.onError(new Throwable("Not a directory!"));
        }
    }
}
