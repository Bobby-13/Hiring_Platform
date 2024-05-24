import React, { useEffect, useState } from "react";
import { IoIosNotificationsOutline, IoIosClose } from "react-icons/io";
import { FaCircleUser } from "react-icons/fa6";
import "./NotificationSlideBar.scss";
import axios from "axios";

const NotificationSlideBar = () => {
  const [isNotificationOpen, setNotificationOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAdminHomeData = async () => {
      try {
        const response = await axios.get(
          `http://192.168.1.20:8081/api/v1/contest/admin/home`
        );
        const notificationData = response.data.object.notifications;
        setNotifications(notificationData);
      } catch (error) {
        console.error("Error fetching admin home data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchAdminHomeData();
  }, []);

  const toggleNotification = () => {
    setNotificationOpen(!isNotificationOpen);
  };

  const closeNotificationBox = () => {
    setNotificationOpen(false);
  };

  const closeNotification = (id) => {
    const updatedNotifications = notifications.filter(
      (notification) => notification.id !== id
    );
    setNotifications(updatedNotifications);
  };

  const clearAllNotifications = () => {
    setNotifications([]);
  };

  return (
    <div className="notificationSidebar">
      <div className="notification-count">
        {notifications.length > 0 ? notifications.length : "0"}
      </div>
      <IoIosNotificationsOutline
        className="notifyicon"
        onClick={toggleNotification}
      >
        <span className="notification-count">
          {notifications.length > 0 ? notifications.length : "0"}
        </span>
      </IoIosNotificationsOutline>
      <div className={`notification-box ${isNotificationOpen ? "open" : ""}`}>
        NOTIFICATION
        <IoIosClose className="close-icon" onClick={closeNotificationBox} />
        {notifications.length > 0 ? (
          notifications.map((notification) => (
            <div key={notification.id} className="notification-item">
              <div className="profile-info">
                <span className="profile-icon"><FaCircleUser /></span>
              </div>
              <div className="Profilecontent">
                <span className="profile-name">User</span>
                <div className="notification-content">
                  {notification.message}
                </div>
                <span className="notification-time">
                  {new Date(notification.createdAt).toLocaleString()}
                </span>
              </div>
              <IoIosClose
                className="remove-icon"
                onClick={() => closeNotification(notification.id)}
              />
            </div>
          ))
        ) : (
          <div className="no-notifications">No notifications</div>
        )}
        {notifications.length > 0 && (
          <span className="clearall" onClick={clearAllNotifications}>
            CLEAR ALL
          </span>
        )}
      </div>
    </div>
  );
};

export default NotificationSlideBar;
