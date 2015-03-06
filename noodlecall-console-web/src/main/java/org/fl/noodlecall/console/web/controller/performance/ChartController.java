package org.fl.noodlecall.console.web.controller.performance;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.fl.noodlecall.console.web.mvc.annotation.RequestParam;
import org.fl.noodlecall.console.web.mvc.annotation.ResponseBody;
import org.fl.noodlecall.monitor.performance.persistence.redis.RedisPerformancePersistence;
import org.fl.noodlecall.monitor.performance.vo.KeyVo;

@Controller
@RequestMapping(value = "monitor/chart")
public class ChartController {
	
	@Autowired
	RedisPerformancePersistence redisPerformancePersistence;
	
	@RequestMapping(value = "/getdatetime")
	@ResponseBody
	public ChartVo getdatetime() throws Exception {
		
		Date now = new Date();
		ChartVo chartVo = new ChartVo();
		chartVo.setTimestamp(now.getTime());
		return chartVo;
	}
	
	@RequestMapping(value = "/querychartsinglenow")
	@ResponseBody
	public List<ChartVo> queryChartSingleNow(@RequestParam KeyVo keyVo, String region) throws Exception {
		long regionLong = region != null && !region.equals("") ? Long.valueOf(region) : 60;
		long nowTime = System.currentTimeMillis();
		return redisPerformancePersistence.queryList(keyVo.toKeyString(), nowTime - regionLong * 60000, nowTime, ChartVo.class);
	}
	
	@RequestMapping(value = "/querychartbganded")
	@ResponseBody
	public List<ChartVo> queryChartBgAndEd(@RequestParam KeyVo keyVo, @RequestParam(type = "date") Date beginTime, @RequestParam(type = "date") Date endTime) throws Exception {
		return redisPerformancePersistence.queryList(keyVo.toKeyString(), beginTime.getTime(), endTime.getTime(), ChartVo.class);
	}
	
	@RequestMapping(value = "/querychartsinglenowlast")
	@ResponseBody
	public List<ChartVo> queryChartSinglenowlast(@RequestParam KeyVo keyVo, String intervalLastTime) throws Exception {
		long intervalLastTimeLong = intervalLastTime != null && !intervalLastTime.equals("") ? Long.valueOf(intervalLastTime) : System.currentTimeMillis();
		return redisPerformancePersistence.queryList(keyVo.toKeyString(), intervalLastTimeLong, Long.MAX_VALUE, ChartVo.class);
	}
	
	public static class ChartVo {
		
		private long totalCount;
		private long overtimeCount;
		private long threshold;
		private long averageTime;
		private long successCount;
		private long timestamp;
		
		public long getTotalCount() {
			return totalCount;
		}
		
		public void setTotalCount(long totalCount) {
			this.totalCount = totalCount;
		}

		public long getOvertimeCount() {
			return overtimeCount;
		}

		public void setOvertimeCount(long overtimeCount) {
			this.overtimeCount = overtimeCount;
		}

		public long getThreshold() {
			return threshold;
		}

		public void setThreshold(long threshold) {
			this.threshold = threshold;
		}
		
		public long getAverageTime() {
			return averageTime;
		}

		public void setAverageTime(long averageTime) {
			this.averageTime = averageTime;
		}

		public long getSuccessCount() {
			return successCount;
		}

		public void setSuccessCount(long successCount) {
			this.successCount = successCount;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}
}