package com.basketstats.basketstats;

import org.json.JSONException;
import org.json.JSONObject;

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

    public String toLog(){
        String ret1 = "_2pm=" + String.valueOf(_2pm) + " _2pa=" + String.valueOf(_2pa) + " _3pm=" + String.valueOf(_3pm) + " _3pa=" + String.valueOf(_3pa);
        String ret2 = " fta=" + String.valueOf(fta) + " ftm=" + String.valueOf(ftm) + " reb=" + String.valueOf(reb) + " ast=" + String.valueOf(ast);
        String ret3 = " pf=" + String.valueOf(pf) + " stl=" + String.valueOf(stl) + " bs=" + String.valueOf(bs) + " to=" + String.valueOf(to);
        return ret1 + ret2 + ret3;
    }

    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("_2pm", _2pm);
            obj.put("_2pa", _2pa);
            obj.put("_3pm", _3pm);
            obj.put("_3pa", _3pa);
            obj.put("fta", fta);
            obj.put("ftm", ftm);
            obj.put("reb", reb);
            obj.put("ast", ast);
            obj.put("pf", pf);
            obj.put("to", to);
            obj.put("stl", stl);
            obj.put("bs", bs);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
