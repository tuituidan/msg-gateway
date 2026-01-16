package com.tuituidan.openhub.mapper;

import com.tuituidan.openhub.bean.vo.LineData;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * HomeMapper.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026-01-12
 */
public interface HomeMapper {

    /**
     * selectAllDataLogCount
     *
     * @return Long
     */
    Long selectAllApiLogCount();

    /**
     * selectAllPushLogCount
     *
     * @return Long
     */
    Long selectAllPushLogCount();

    /**
     * selectPushLogCount
     *
     * @param status status
     * @param date date
     * @return Long
     */
    Long selectPushLogCount(@Param("status") String status, @Param("date") String date);

    /**
     * todayDataLogLine
     *
     * @param curDate curDate
     * @return List
     */
    List<LineData> todayApiLogLine(@Param("curDate") String curDate);

    /**
     * todayPushLogLine
     *
     * @param curDate curDate
     * @return List
     */
    List<LineData> todayPushLogLine(@Param("curDate") String curDate);

    /**
     * lastMonthDataLogLine
     *
     * @param lastMonthDate lastMonthDate
     * @return List
     */
    List<LineData> lastMonthApiLogLine(@Param("lastMonthDate") String lastMonthDate);

    /**
     * lastMonthPushLogLine
     *
     * @param lastMonthDate lastMonthDate
     * @return List
     */
    List<LineData> lastMonthPushLogLine(@Param("lastMonthDate") String lastMonthDate);

    /**
     * selectApiLogCount
     *
     * @return List
     */
    List<LineData> selectApiLogCount();

}
