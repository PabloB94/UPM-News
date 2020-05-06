package es.upm.etsiinf.upmnews;

import java.util.List;

import es.upm.etsiinf.upmnews.model.Article;

interface AsyncResponse {
    void processFinish(List<Article> output);
}
