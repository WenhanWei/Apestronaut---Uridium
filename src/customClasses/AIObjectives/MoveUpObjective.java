package customClasses.AIObjectives;

import components.*;
import GameObjects.*;
 
public class MoveUpObjective extends Objective {

    GameObject thisPlayer;
    Transform thisTransform;
    AIPlayerController thisController;

    GameObject targetPlayer;
    Transform targetTransform;
    boolean doubleJump;

    int targetXDir = 0;
    float targetX;

    public MoveUpObjective(GameObject thisPl, boolean mustDoubleJump) {
        thisPlayer = thisPl;
        thisTransform = thisPlayer.GetTransform();
        thisController = thisPlayer.GetComponent(AIPlayerController.class);

        targetPlayer = thisController.chooseTargetPlayer();
        targetTransform = targetPlayer.GetTransform();

        doubleJump = mustDoubleJump;
    }

    @Override
    public void Update() {
        switch (currentStep) {
            case 0:
                decideXDir();
                break;
            case 1:
                moveToHeadroom(); // move to a space where there's space above the AI
                break;
            case 2:
                slowDown();
                break;
            case 3:
                thisController.jump();
                currentStep = 4;
                break;
            case 4:
                if (hasWaited(10f)) {
                    currentStep = 5;
                }
                break;
            case 5:
                thisController.jump();
                setTargetX(); // target is moving left or right
                break;
            case 6:
                moveToTarget();
                break;
        }
    }

    private void decideXDir() {
        if (Math.abs(thisTransform.pos.x - targetTransform.pos.x) < 250) {
            targetXDir = -thisController.xDir;
        } else {
            targetXDir = thisController.xDir;
        }
        currentStep = 1;
    }

    private void moveToHeadroom() {
        if (thisController.checkPlatformAbove()) {
            if (targetXDir == 1) {
                thisController.moveRight();
            } else {
                thisController.moveLeft();
            }
        } else {
            currentStep = 2;
        }
    }

    private void setTargetX() {
        if (targetXDir == 1) {
            targetX = thisTransform.pos.x - 20;
        } else {
            targetX = thisTransform.pos.x + 20;
        }
        currentStep = 6;
    }

    private void moveToTarget() {
        if (targetXDir == 1 && targetX < thisTransform.pos.x) {
            thisController.moveLeft();
        } else if (targetXDir == -1 && targetX > thisTransform.pos.x) {
            thisController.moveRight();
        } else {

            nextObjective = new AttackObjective(thisPlayer);
        }
    }

    private void slowDown() {
        if (thisController.vel.x != 0) {
            thisController.decelerate();
        } else {
            if (doubleJump) {
                currentStep = 3;
            } else {
                currentStep = 5;
            }
        }
    }
}
