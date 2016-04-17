package com.basketstats.basketstats;

public class PlayerRecord extends Object {
    public int _2pm, _2pa, _3pm, _3pa, ftm, fta, pts, reb, ast, pf, stl, bs, to;

    public PlayerRecord(){
        _2pm = _2pa = _3pm = _3pa = ftm = fta = pts = reb = ast = pf = stl = bs = to = 0;
    }

    public void _2pm(int i){
        _2pm += i;
        _2pa += i;
        pts += 2 * i;
    }

    public void _2pa(int i){ _2pa += i; }

    public void _3pm(int i){
        _3pm += i;
        _3pa += i;
        pts += 3 * i;
    }

    public void _3pa(int i){ _3pa += i; }

    public void ftm(int i){
        ftm += i;
        fta += i;
        pts += i;
    }

    public void fta(int i){ fta += i; }
    public void reb(int i){ reb += i; }
    public void ast(int i){ ast += i; }
    public void pf(int i){ pf += i; }
    public void stl(int i){ stl += i; }
    public void bs(int i){ bs += i; }
    public void to(int i){ to += i; }
}
