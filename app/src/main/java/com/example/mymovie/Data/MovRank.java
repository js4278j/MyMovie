package com.example.mymovie.Data;

public class MovRank {
    //순번
    private String rnum;
    //순위
    private String rank;
    //전일대비 증감
    private String rankInten;
    //영화코드
    private String movieCd;
    //영화이름
    private String movieNm;
    //개봉일
    private String openDt;
    //해당일 관객수
    private String audiCnt;
    //누적 관객수
    private String audiAcc;
    //스크린 수
    private String scrnCnt;

    public MovRank(String rnum, String rank, String rankInten, String movieCd, String movieNm, String openDt, String audiCnt, String audiAcc, String scrnCnt) {
        this.rnum = rnum;
        this.rank = rank;
        this.rankInten = rankInten;
        this.movieCd = movieCd;
        this.movieNm = movieNm;
        this.openDt = openDt;
        this.audiCnt = audiCnt;
        this.audiAcc = audiAcc;
        this.scrnCnt = scrnCnt;
    }

    public String getRnum() {
        return rnum;
    }

    public void setRnum(String rnum) {
        this.rnum = rnum;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRankInten() {
        return rankInten;
    }

    public void setRankInten(String rankInten) {
        this.rankInten = rankInten;
    }

    public String getMovieCd() {
        return movieCd;
    }

    public void setMovieCd(String movieCd) {
        this.movieCd = movieCd;
    }

    public String getMovieNm() {
        return movieNm;
    }

    public void setMovieNm(String movieNm) {
        this.movieNm = movieNm;
    }

    public String getOpenDt() {
        return openDt;
    }

    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }

    public String getAudiCnt() {
        return audiCnt;
    }

    public void setAudiCnt(String audiCnt) {
        this.audiCnt = audiCnt;
    }

    public String getAudiAcc() {
        return audiAcc;
    }

    public void setAudiAcc(String audiAcc) {
        this.audiAcc = audiAcc;
    }

    public String getScrnCnt() {
        return scrnCnt;
    }

    public void setScrnCnt(String scrnCnt) {
        this.scrnCnt = scrnCnt;
    }
}
