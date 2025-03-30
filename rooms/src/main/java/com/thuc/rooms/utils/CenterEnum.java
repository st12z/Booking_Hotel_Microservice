package com.thuc.rooms.utils;

import java.util.HashMap;
import java.util.Map;

public enum CenterEnum {
    HA_NOI(Map.of(21.02791593061257, 105.83194437707638)),
    HO_CHI_MINH(Map.of(10.735967856544661, 106.58826009383338)),
    DA_NANG(Map.of(16.0521139283001, 108.2008685071329)),
    NHA_TRANG(Map.of(12.24972835991412, 109.17508656432112)),
    DA_LAT(Map.of(11.957308026781579, 108.43183563737935));
    private  Map<Double, Double> map = new HashMap<Double, Double>();
    private CenterEnum(Map<Double, Double> map) {
        this.map=map;
    }
    private Map<Double, Double> getMap() {
        return map;
    }

}
