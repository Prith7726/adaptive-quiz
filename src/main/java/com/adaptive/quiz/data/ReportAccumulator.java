package com.adaptive.quiz.data;

import java.util.ArrayList;
import java.util.List;

public class ReportAccumulator {

    private final List<ReportData> datas = new ArrayList<>();
    public void add(ReportData data) {
        datas.add(data);
    }


}
