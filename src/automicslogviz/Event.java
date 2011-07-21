/**
 * Copyright 2010 The University of Nottingham
 * 
 * This file is part of automicslogviz.
 *
 *  automicslogviz is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  automicslogviz is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with automicslogviz.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package automicslogviz;

/**
 * @author cmg
 *
 */
public class Event {
	//taskType,taskId,init,gpsTagId,notificationType,notificationTs,reminderCount,notficationAcceptTs,acceptanceTime,taskAcceptTs,taskAcceptTime,taskComp,taskCompTs,taskCompTime,responseTime,receptRating 
	private String taskType;
	private int taskId;
	private String init;
	private int gpsTagId;
	private String notificationType;
	private Long notificationTs;
	private Integer reminderCount;
	private Long notficationAcceptTs;
	private Integer acceptanceTime;
	private Long taskAcceptTs;
	private Integer taskAcceptTime;
	private Integer taskComp;
	private Long taskCompTs;
	private Integer taskCompTime;
	private Integer responseTime;
	private Integer receptRating;
	public Event() {
	}
	/**
	 * @param taskType
	 * @param taskId
	 * @param init
	 * @param gpsTagId
	 * @param notificationType
	 * @param notificationTs
	 * @param reminderCount
	 * @param notficationAcceptTs
	 * @param acceptanceTime
	 * @param taskAcceptTs
	 * @param taskAcceptTime
	 * @param taskComp
	 * @param taskCompTs
	 * @param taskCompTime
	 * @param responseTime
	 * @param receptRating
	 */
	public Event(String taskType, int taskId, String init, int gpsTagId,
			String notificationType, Long notificationTs, Integer reminderCount,
			Long notficationAcceptTs, int acceptanceTime, Long taskAcceptTs,
			int taskAcceptTime, int taskComp, Long taskCompTs,
			int taskCompTime, int responseTime, int receptRating) {
		super();
		this.taskType = taskType;
		this.taskId = taskId;
		this.init = init;
		this.gpsTagId = gpsTagId;
		this.notificationType = notificationType;
		this.notificationTs = notificationTs;
		this.reminderCount = reminderCount;
		this.notficationAcceptTs = notficationAcceptTs;
		this.acceptanceTime = acceptanceTime;
		this.taskAcceptTs = taskAcceptTs;
		this.taskAcceptTime = taskAcceptTime;
		this.taskComp = taskComp;
		this.taskCompTs = taskCompTs;
		this.taskCompTime = taskCompTime;
		this.responseTime = responseTime;
		this.receptRating = receptRating;
	}
	/**
	 * @return the taskType
	 */
	public String getTaskType() {
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	/**
	 * @return the taskId
	 */
	public int getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the init
	 */
	public String getInit() {
		return init;
	}
	/**
	 * @param init the init to set
	 */
	public void setInit(String init) {
		this.init = init;
	}
	/**
	 * @return the gpsTagId
	 */
	public int getGpsTagId() {
		return gpsTagId;
	}
	/**
	 * @param gpsTagId the gpsTagId to set
	 */
	public void setGpsTagId(int gpsTagId) {
		this.gpsTagId = gpsTagId;
	}
	/**
	 * @return the notificationType
	 */
	public String getNotificationType() {
		return notificationType;
	}
	/**
	 * @param notificationType the notificationType to set
	 */
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	/**
	 * @return the notificationTs
	 */
	public Long getNotificationTs() {
		return notificationTs;
	}
	/**
	 * @param notificationTs the notificationTs to set
	 */
	public void setNotificationTs(Long notificationTs) {
		this.notificationTs = notificationTs;
	}
	/**
	 * @return the reminderCount
	 */
	public Integer getReminderCount() {
		return reminderCount;
	}
	/**
	 * @param reminderCount the reminderCount to set
	 */
	public void setReminderCount(Integer reminderCount) {
		this.reminderCount = reminderCount;
	}
	/**
	 * @return the notficationAcceptTs
	 */
	public Long getNotficationAcceptTs() {
		return notficationAcceptTs;
	}
	/**
	 * @param notficationAcceptTs the notficationAcceptTs to set
	 */
	public void setNotficationAcceptTs(Long notficationAcceptTs) {
		this.notficationAcceptTs = notficationAcceptTs;
	}
	/**
	 * @return the acceptanceTime
	 */
	public Integer getAcceptanceTime() {
		return acceptanceTime;
	}
	/**
	 * @param acceptanceTime the acceptanceTime to set
	 */
	public void setAcceptanceTime(Integer acceptanceTime) {
		this.acceptanceTime = acceptanceTime;
	}
	/**
	 * @return the taskAcceptTs
	 */
	public Long getTaskAcceptTs() {
		return taskAcceptTs;
	}
	/**
	 * @param taskAcceptTs the taskAcceptTs to set
	 */
	public void setTaskAcceptTs(Long taskAcceptTs) {
		this.taskAcceptTs = taskAcceptTs;
	}
	/**
	 * @return the taskAcceptTime
	 */
	public int getTaskAcceptTime() {
		return taskAcceptTime;
	}
	/**
	 * @param taskAcceptTime the taskAcceptTime to set
	 */
	public void setTaskAcceptTime(Integer taskAcceptTime) {
		this.taskAcceptTime = taskAcceptTime;
	}
	/**
	 * @return the taskComp
	 */
	public Integer getTaskComp() {
		return taskComp;
	}
	/**
	 * @param taskComp the taskComp to set
	 */
	public void setTaskComp(Integer taskComp) {
		this.taskComp = taskComp;
	}
	/**
	 * @return the taskCompTs
	 */
	public Long getTaskCompTs() {
		return taskCompTs;
	}
	/**
	 * @param taskCompTs the taskCompTs to set
	 */
	public void setTaskCompTs(Long taskCompTs) {
		this.taskCompTs = taskCompTs;
	}
	/**
	 * @return the taskCompTime
	 */
	public Integer getTaskCompTime() {
		return taskCompTime;
	}
	/**
	 * @param taskCompTime the taskCompTime to set
	 */
	public void setTaskCompTime(Integer taskCompTime) {
		this.taskCompTime = taskCompTime;
	}
	/**
	 * @return the responseTime
	 */
	public Integer getResponseTime() {
		return responseTime;
	}
	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(Integer responseTime) {
		this.responseTime = responseTime;
	}
	/**
	 * @return the receptRating
	 */
	public Integer getReceptRating() {
		return receptRating;
	}
	/**
	 * @param receptRating the receptRating to set
	 */
	public void setReceptRating(Integer receptRating) {
		this.receptRating = receptRating;
	}

}
