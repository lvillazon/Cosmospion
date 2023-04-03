package Legacy;

public class PIDController {
    private double kp;
    private double ki;
    private double kd;
    private double integral;
    private double previousError;

    public PIDController(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.integral = 0;
        this.previousError = 0;
    }

    public double update(double error, double dt) {
        integral += error * dt;
        double derivative = (error - previousError) / dt;
        previousError = error;
        return kp * error + ki * integral + kd * derivative;
    }
}
