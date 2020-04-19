package customClasses.AIObjectives;

import logic.Time;

public class Objective {

    public Objective nextObjective = this;
    int currentStep = 0;

    float waitTime = Float.MAX_VALUE;

    public void Update() {
    }

    public void nextStep() {
        currentStep++;
    }

    public void goToStep(int step) {
        currentStep = step;
    }

    public Objective getNewObjective() {
        if (nextObjective == this) {
            return null;
        } else {
            return nextObjective;
        }
    }

    public boolean hasWaited(float time) {
        if (waitTime == Float.MAX_VALUE) {
            waitTime = time;
        }
        waitTime -= Time.deltaTime;
        if (waitTime <= 0) {
            waitTime = Float.MAX_VALUE;
            return true;
        } else {
            return false;
        }
    }
}
