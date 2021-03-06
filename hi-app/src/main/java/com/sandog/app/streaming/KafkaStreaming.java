package com.sandog.app.streaming;

import com.alibaba.fastjson.JSON;
import com.sandog.app.sink.SinkToMySql;
import com.sandog.common.model.Student;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author sandog
 * @date 2019/7/15
 */
public class KafkaStreaming {

    public static void main(String [] args) throws Exception {

        String hostname = "127.0.0.1";
        Integer port = 9000;

        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 获取数据
        DataStreamSource<String> stream = env.socketTextStream(hostname, port);
        SingleOutputStreamOperator<Student> streamOperator = stream.flatMap(new StudentSplitter());
        streamOperator.addSink(new SinkToMySql()); //数据 sink 到 mysql
        env.execute("Java WordCount from SocketTextStream Example");
    }

    public static final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) {
            String[] tokens = s.toLowerCase().split("\\W+");

            for (String token: tokens) {
                if (token.length() > 0) {
                    collector.collect(new Tuple2<>(token, 1));
                }
            }
        }

    }

    public static final class StudentSplitter implements FlatMapFunction<String, Student> {

        @Override
        public void flatMap(String s, Collector<Student> collector) throws Exception {
            Student student = Student.builder()
                    .id(1L)
                    .name("傅红雪").build();
            try {
                System.out.println(s);
                student = JSON.parseObject(s, Student.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            collector.collect(student);
        }
    }

}
