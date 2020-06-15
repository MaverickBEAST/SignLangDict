package com.example.signlangdict;

public class uploadinfo {
    public String title;
    public String meaning;
    public String image;
    public String search;
    public uploadinfo(){}

    public uploadinfo(String title, String search, String url, String meaning) {
        this.title = title;
        this.search= search;
        this.image = url;
        this.meaning = meaning;
    }

    public String gettitle() {
        return title;
    }
    public String getSearch() {
        return search;
    }
    public String getimage() {
        return image;
    }
    public String getmeaning() {
        return meaning;
    }
}
