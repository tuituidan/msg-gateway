package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.entity.SysApp;
import com.tuituidan.openhub.bean.entity.SysEntryApi;
import com.tuituidan.openhub.bean.vo.LineData;
import com.tuituidan.openhub.mapper.HomeMapper;
import com.tuituidan.openhub.mapper.SysAppMapper;
import com.tuituidan.openhub.mapper.SysEntryApiMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * HomeService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026-01-13
 */
@Service
public class HomeService {

    @Resource
    private HomeMapper homeMapper;

    @Resource
    private SysEntryApiMapper sysEntryApiMapper;

    @Resource
    private SysAppMapper sysAppMapper;

    /**
     * selectAllApiLogCount
     *
     * @return Long
     */
    public Long selectAllApiLogCount() {
        return homeMapper.selectAllApiLogCount();
    }

    /**
     * selectAllPushLogCount
     *
     * @return Long
     */
    public Long selectAllPushLogCount() {
        return homeMapper.selectAllPushLogCount();
    }

    /**
     * selectPushLogCount
     *
     * @param status status
     * @return Long
     */
    public Long selectPushLogCount(String status) {
        return homeMapper.selectPushLogCount(status, LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    /**
     * todayApiLogLine
     *
     * @return Map
     */
    public List<LineData> todayApiLogLine() {
        List<LineData> lineData = homeMapper.todayApiLogLine(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        return buildLineData(lineData, String::valueOf,
                LocalDateTime.now().get(ChronoField.HOUR_OF_DAY));
    }

    /**
     * todayPushLogLine
     *
     * @return Map
     */
    public Map<String, List<LineData>> todayPushLogLine() {
        Map<Long, String> legendMap = sysAppMapper.selectAll().stream()
                .collect(Collectors.toMap(SysApp::getId, SysApp::getAppName));
        List<LineData> lineData = homeMapper.todayPushLogLine(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        return buildLineData(lineData, legendMap, String::valueOf,
                LocalDateTime.now().get(ChronoField.HOUR_OF_DAY));
    }

    /**
     * lastMonthApiLogLine
     *
     * @return Map
     */
    public List<LineData> lastMonthApiLogLine() {
        LocalDate firstDay = LocalDate.now().plusDays(-30);
        List<LineData> lineData = homeMapper.lastMonthApiLogLine(firstDay.format(DateTimeFormatter.BASIC_ISO_DATE));
        return buildLineData(lineData,
                index -> firstDay.plusDays(index).format(DateTimeFormatter.ofPattern("MM-dd")), 30);
    }

    /**
     * lastMonthPushLogLine
     *
     * @return Map
     */
    public Map<String, List<LineData>> lastMonthPushLogLine() {
        LocalDate firstDay = LocalDate.now().plusDays(-30);
        Map<Long, String> legendMap = sysAppMapper.selectAll().stream()
                .collect(Collectors.toMap(SysApp::getId, SysApp::getAppName));
        List<LineData> lineData = homeMapper.lastMonthPushLogLine(firstDay.format(DateTimeFormatter.BASIC_ISO_DATE));
        return buildLineData(lineData, legendMap,
                index -> firstDay.plusDays(index).format(DateTimeFormatter.ofPattern("MM-dd")), 30);
    }

    /**
     * selectApiLogCount
     *
     * @return List
     */
    public List<LineData> selectApiLogCount() {
        Map<Long, Integer> dataMap = homeMapper.selectApiLogCount().stream()
                .collect(Collectors.toMap(LineData::getId, LineData::getYdata));
        List<SysEntryApi> apiList = sysEntryApiMapper.selectAll();
        List<LineData> resultList = new ArrayList<>();
        for (SysEntryApi item : apiList) {
            resultList.add(new LineData().setXdata(item.getName())
                    .setYdata(dataMap.getOrDefault(item.getId(), 0)));
        }
        return resultList;
    }

    private Map<String, List<LineData>> buildLineData(List<LineData> lineData,
            Map<Long, String> legendMap, IntFunction<String> xdataFunc, int length) {
        Map<Long, List<LineData>> dataMap = lineData.stream().collect(Collectors.groupingBy(LineData::getId));
        Map<String, List<LineData>> result = new HashMap<>(dataMap.size());
        for (Entry<Long, String> legendEntry : legendMap.entrySet()) {
            List<LineData> values = buildLineData(dataMap.getOrDefault(legendEntry.getKey(),
                    new ArrayList<>()), xdataFunc, length);
            result.put(legendEntry.getValue(), values);
        }
        return result;
    }

    private List<LineData> buildLineData(List<LineData> lineData, IntFunction<String> xdataFunc, int length) {
        Map<String, Integer> sourceMap = lineData.stream().collect(Collectors.toMap(LineData::getXdata,
                LineData::getYdata));
        List<LineData> result = new ArrayList<>();
        for (int i = 0; i <= length; i++) {
            String xdata = xdataFunc.apply(i);
            result.add(new LineData()
                    .setXdata(xdata)
                    .setYdata(sourceMap.getOrDefault(xdata, 0)));
        }
        return result;
    }

}
