package com.yonyou.news;

import xyy.base.TextUtils;
import xyy.http.framework.XBodyData;
import xyy.http.framework.XBusinessException;
import xyy.http.framework.XJSONServlet;
import xyy.http.framework.controller.XHttpController;
import java.util.UUID;

/**
 * Created by xyy on 2018/6/27.
 */
public class NewsController extends XHttpController {

    private static NewsCollection news = new NewsCollection();

    static {
        news.put(new NewsObject(UUID.randomUUID().toString(), "新时代共青团工作怎么做？习近平这样指明方向", "icon.png", "\t央视网消息：青年是祖国的未来、民族的希望。共青团工作要紧紧围绕党和国家工作大局，心系青年、心向青年，引导青少年勇做走在时代前列的奋进者、开拓者、奉献者。\n\t党的十八大以来，习近平总书记围绕青少年和共青团工作发表了一系列重要论述，深刻阐释了新形势下青少年和共青团工作的重大理论和实践问题，为当代青年的成长道路指引前程，为共青团工作指明了方向。\n\t团组织——要走在时代前列 走在青年前列.加强党委对团工作的领导各级党委要从巩固和扩大党执政的青年群众基础的战略高度，加强对团的工作的领导，为团组织提供良好工作环境和条件。\n\t把握住根本性问题.团的工作要把握住根本性问题，把培养中国特色社会主义事业建设者和接班人作为根本任务，把巩固和扩大党执政的青年群众基础作为政治责任，把围绕中心、服务大局作为工作主线。\n\t站在理想信念这个制高点上.团的工作要把握住广大青年的脉搏。要提高团的吸引力和凝聚力，关键是要高举理想信念的旗帜。共青团要做好青年思想引导工作、增强吸引力和凝聚力，必须站在理想信念这个制高点上。"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "建设美丽中国，在习近平生态文明思想引领下", "icon.png", "\t24日公布的《中共中央国务院关于全面加强生态环境保护坚决打好污染防治攻坚战的意见》（以下简称《意见》）提出，坚决打赢蓝天保卫战，着力打好碧水保卫战，扎实推进净土保卫战。《意见》还提出，坚持建设美丽中国全民行动。美丽中国是人民群众共同参与共同建设共同享有的事业。建设美丽中国，每个人都是行动者。习近平总书记不仅身体力行，还是美丽中国的引领者。\n\t习近平总书记高度重视生态文明建设，多次就此作出重要指示。在5月18日至19日召开的全国生态环境保护大会上，总书记更是强调，生态文明建设是关系中华民族永续发展的根本大计。正是在习近平总书记的大力推动之下，我国先后出台了“大气十条”、“水十条”、“土十条”以及《农村人居环境整治三年行动方案》等政策措施，全方位推进美丽中国建设。"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "落户天津后两年不买房就清户？官方：不存在","icon.png","\\t近日，有传言称“新落户天津后，将户口存放在人才市场集体户的，如果两年内不买房并将户口迁走，户口将会被清理并打回原籍”，这一说法引发社会关注，并成为部分房地产销售人员的营销噱头。为避免群众误解误读相关政策，记者采访了我市相关主管部门。\\n\\t对于“两年不买房就清户”传言，市公安局人口管理总队相关负责同志表示：“并不存在‘两年不买房就清户’。按照户籍管理有关规定，办理户口迁移需要公民本人提出申请，除对弄虚作假骗取落户资格的人员外，公安机关不会将其户口强制迁出或者注销。”\\n\\t市房地产主管部门提醒广大购房群众，不要随便听信非正式渠道传播的流言，有政策疑问一定要到相关政府部门进行咨询。市国土房管局相关负责同志接受采访时表示：“对于蓄意传播散布谣言、制造市场恐慌、扰乱房地产市场秩序的房地产开发企业、中介机构等，市国土房管局将会同公安、市场监管等部门，按照《天津市政府办公厅关于进一步做好我市房地产市场调控工作的通知》的相关要求，坚决采取公开曝光、暂停网签、停业整顿、行政处罚、限制参与土地竞买等惩戒措施，严肃查处。涉及违法的，将移送有关部门，严肃追究相关机构和个人法律责任。”"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "世界杯：西班牙2:2摩洛哥 凭进球优势小组第一出线","icon.png","无内容"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "中俄艺术家内蒙古呈现“中西合璧”音乐盛宴","icon.png","无内容"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "内蒙古体育系统强化信访责任落实","icon.png","无内容"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "内蒙古司法鉴定开启行业合作新模式","icon.png","无内容"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "内蒙古全区三项先行经济指标稳步增长","icon.png","无内容"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "内蒙古:精准梳理问题线索 加强信访举报跟踪督办","icon.png","无内容"));
        news.put(new NewsObject(UUID.randomUUID().toString(), "2018内蒙古自治区新媒体大会召开 正北方网获多项大奖","icon.png","无内容"));

    }


    public String getNewsList(XBodyData data) throws XBusinessException {
        String key = data.getString("key").trim();
        if (TextUtils.isEmpty(key)) {
            return XJSONServlet.getSUCCESSResult(news.toJSONArray(false));
        } else {
            return XJSONServlet.getSUCCESSResult(news.find(key).toJSONArray());
        }
    }

    public String getNews(XBodyData data) throws XBusinessException {
        String code = data.getString("code").trim();
        NewsObject rs = news.get(code);
        if(rs==null) {
            return XJSONServlet.getERRORResult("not found - " + code);
        }
        return XJSONServlet.getSUCCESSResult(rs.toJSONObject());
    }
}
