package com.yonyou.xjson.controller;

import com.yonyou.digi.DigiObject;
import com.yonyou.digi.game.DigiGame;
import com.yonyou.digi.game.GamePerson;
import com.yonyou.digi.report.DigiReport;
import org.json.JSONObject;
import xyy.http.framework.XBodyData;
import xyy.http.framework.XBusinessException;
import xyy.http.framework.XJSONServlet;
import xyy.http.framework.controller.XHttpController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xyy on 2018/6/12.
 */
public class DigitizeController extends XHttpController {

    public static final String TOKEN = "token";
    private static final long TIME_OUT = 1000 * 60 * 60 * 8;  //8小时后删除比赛资料
    private static HashMap<String, DigiGame> games = new HashMap<String, DigiGame>();
    private static HashMap<String, DigiReport> reports = new HashMap<String, DigiReport>();

    public String createGame(XBodyData data) throws XBusinessException {
        String uuid = data.getString(TOKEN, "none");
        if ("none".equals(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        games.put(uuid, new DigiGame(uuid));

        JSONObject rs = new JSONObject();
        rs.put("token", uuid);

        clearTimeOut(games);

        return XJSONServlet.getSUCCESSResult(rs);
    }

    public String getGameList(XBodyData data) throws XBusinessException {
        return XJSONServlet.getSUCCESSResult(games);
    }

    private void clearTimeOut(Map<String, ? extends DigiObject> data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (data.size() < 100) {
                    //数量小于100时，不进行 Cache 清理
                    return;
                }

                ArrayList<String> timeouts = new ArrayList<>();
                for (String key : data.keySet()) {
                    DigiObject item = data.get(key);
                    if (System.currentTimeMillis() - item.getCreateTime() > DigitizeController.TIME_OUT) {
                        timeouts.add(key);
                    }
                }
                for (String key : timeouts) {
                    data.remove(key);
                }
            }
        }).start();

    }

//    private void clearTimeOutReport() {
//        ArrayList<String> timeouts = new ArrayList<>();
//        for (String key : reports.keySet()) {
//            DigiReport item = reports.get(key);
//            if (System.currentTimeMillis() - item.getCreateTime() > DigitizeController.TIME_OUT) {
//                timeouts.add(key);
//            }
//        }
//        for (String key : timeouts) {
//            reports.remove(key);
//        }
//    }

    public String getGameInfo(XBodyData data) throws XBusinessException {
        String token = data.getString(TOKEN);
        DigiGame game = checkDigiGame(token);
        return XJSONServlet.getSUCCESSResult(game);
    }

    private DigiGame checkDigiGame(String token) throws XBusinessException {
        DigiGame game = games.get(token);
        if (game == null) {
            throw new XBusinessException("不存在的游戏 token - " + token);
        }
        return game;
    }

    public String joinGame(XBodyData data) throws XBusinessException {
        String token = data.getString(TOKEN);
        DigiGame game = checkDigiGame(token);
        if (game.getStatus() != DigiGame.READY) {
            return XJSONServlet.getERRORResult("比赛已开始或已结束");
        }
        String pId = data.getString("id");
        String pName = data.getString("name");
        String pImage = data.getString("photo");
        String photoUrl = data.getString("purl");
        GamePerson p = new GamePerson();
        p.setId(pId);
        p.setName(pName);
        if ("".equals(photoUrl)) {
            p.setPhotoUrl(pImage);
        } else {
            p.setPhotoUrl(photoUrl);
        }

        game.addPerson(p);
        return XJSONServlet.getSUCCESSResult(game);
    }

    public String beginGame(XBodyData data) throws XBusinessException {
        String token = data.getString(TOKEN);
        DigiGame game = checkDigiGame(token);
        if (game.getPersons().size() == 0) {
            return XJSONServlet.getERRORResult("一个人都没参加？");
        }
        if (game.getPersons().size() == 1) {
            return XJSONServlet.getERRORResult("自己和自己比赛？");
        }
        game.setStatus(DigiGame.START);
        return XJSONServlet.getSUCCESSResult(game);
    }

    public String running(XBodyData data) throws XBusinessException {
        String token = data.getString(TOKEN);
        DigiGame game = checkDigiGame(token);
        if (game.getStatus() == DigiGame.READY) {
            return XJSONServlet.getERRORResult("比赛未开始");
        }
        if (game.getStatus() == DigiGame.END) {
            return XJSONServlet.getSUCCESSResult(game);
        }

        int velocity = data.getInt("velocity");
        String pId = data.getString("runner");

        for (GamePerson person : game.getPersons()) {
            if (person.getId().equals(pId)) {
                person.setPosition(person.getPosition() + velocity);
                if (person.getPosition() >= 100) {
                    person.setPosition(100);
                    game.setStatus(DigiGame.END);
                }
                break;
            }
        }
        return XJSONServlet.getSUCCESSResult(game);
    }

    public String getGameResult(XBodyData data) throws XBusinessException {
        String token = data.getString(TOKEN);
        return XJSONServlet.getSUCCESSResult(checkDigiGame(token));
    }

    public String createReport(XBodyData data) throws XBusinessException {
        String uuid = data.getString(TOKEN, "none");
        if ("none".equals(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        DigiReport rpt = new DigiReport(uuid);
        rpt.setName(data.getString("name"));
        rpt.setCompany(data.getString("company"));
        rpt.setPhoto(data.getString("photo"));
        rpt.setSign(data.getString("sign"));
        rpt.setAmounts(data.getLong("amounts", 0L));
        reports.put(uuid, rpt);


        JSONObject rs = new JSONObject();
        rs.put("token", uuid);

        clearTimeOut(reports);

        return XJSONServlet.getSUCCESSResult(rs);
    }

    public String getReport(XBodyData data) throws XBusinessException {
        String token = data.getString(TOKEN);
        DigiReport rpt = checkDigiReport(token);

        return XJSONServlet.getSUCCESSResult(rpt);
    }

    private DigiReport checkDigiReport(String token) throws XBusinessException {
        DigiReport rpt = reports.get(token);
        if (rpt == null) {
            throw new XBusinessException("不存在的喜报 token - " + token);
        }
        return rpt;
    }

}
