package org.usfirst.frc.team5968.robot;

public class PortMap {
    
    public enum USB {
        LEFT, 
        RIGHT
    }
    
    public enum CAN {
        LEFT_MOTOR_CONTROLLER_LEAD,
        LEFT_MOTOR_CONTROLLER_FOLLOWER,
        RIGHT_MOTOR_CONTROLLER_LEAD,
        RIGHT_MOTOR_CONTROLLER_FOLLOWER,
        PCM
    }
    
    public static int portOf(USB usbDevice) {
        switch(usbDevice) {
        case LEFT:
            return 0;
        case RIGHT:
            return 1;
        default:
            return -1;
        }
    }
    
    public static int portOf(CAN canDevice) {
        switch(canDevice) {
        case LEFT_MOTOR_CONTROLLER_LEAD:
            return 1;
        case LEFT_MOTOR_CONTROLLER_FOLLOWER:
            return 2;
        case RIGHT_MOTOR_CONTROLLER_LEAD:
            return 3;
        case RIGHT_MOTOR_CONTROLLER_FOLLOWER:
            return 4;
        default:
            return -1;
        }
    }
}
