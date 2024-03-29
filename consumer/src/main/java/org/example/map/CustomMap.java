package org.example.map;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardMeterWrapper;
import org.apache.flink.metrics.Meter;
import org.example.utils.Generator;
import org.example.utils.SomeAlgs;

public class CustomMap extends RichMapFunction<String, String> {
    private Meter meter;

    @Override
    public String map(String s) {
        meter.markEvent();
        String pattern = Generator.getRString((int)Generator.getRNInRange(2,10));
        return "KMP per "+ pattern+": " + SomeAlgs.KMP(s, pattern);
        //return "MESSAGE SIZE: " + s.length();
    }

    @Override
    public void open(Configuration config) {
        com.codahale.metrics.Meter dropwizardMeter = new com.codahale.metrics.Meter();

        this.meter = getRuntimeContext()
                .getMetricGroup()
                .meter("myMeter", new DropwizardMeterWrapper(dropwizardMeter));
    }
}