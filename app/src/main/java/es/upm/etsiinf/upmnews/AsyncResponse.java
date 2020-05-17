package es.upm.etsiinf.upmnews;

import es.upm.etsiinf.upmnews.model.Article;

//interface of the methods launching async tasks
public interface AsyncResponse {
    void processFinish(Boolean output);
    void processData(Article output);
}
