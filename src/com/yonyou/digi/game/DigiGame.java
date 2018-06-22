package com.yonyou.digi.game;

import com.yonyou.digi.DigiObject;
import xyy.http.framework.XBusinessException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyy on 2018/6/12.
 */
public class DigiGame extends DigiObject {

    public static final int START = 1;
    public static final int END = 2;
    public static final int READY = 0;


    private int status = 0; //0未开始，1已开始，2已结束


    private List<GamePerson> persons = new ArrayList<>();

    public DigiGame(String token) {
        super(token);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GamePerson> getPersons() {
        return persons;
    }

    public void setPersons(List<GamePerson> persons) {
        this.persons = persons;
    }


    public void addPerson(GamePerson person) throws XBusinessException {
        if (person.getId() == null || "".equals(person.getId())) {
            throw new XBusinessException("ID 不能为空");
        }
        if (getPersons().size() >= 5) {
            throw new XBusinessException("报名已满，下局请早");
        }
        for (GamePerson item : getPersons()) {
            if (item.getId().equals(person.getId())) {
                throw new XBusinessException("ID 重复");
            }
            if (item.getName().equals(person.getName())) {
                person.setName(person.getName() + "-" + person.getId());
            }
        }
        getPersons().add(person);
    }
}
