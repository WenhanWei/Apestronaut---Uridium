package customClasses.AIObjectives;

import components.*;
import GameObjects.*;
 
public class MoveDownObjective extends Objective {

    GameObject thisPlayer;
    Transform thisTransform;
    AIPlayerController thisController;

    GameObject targetPlayer;
    Transform targetTransform;

    int targetXDir = 0;
    float targetX;

    public MoveDownObjective(GameObject thisPl) {
        thisPlayer = thisPl;
        thisTransform = thisPlayer.GetTransform();
        thisController = thisPlayer.GetComponent(AIPlayerController.class);

        targetPlayer = thisController.chooseTargetPlayer();
        targetTransform = targetPlayer.GetTransform();
    }

    @Override
    public void Update() {
        switch (currentStep) {
            case 0:
                decideXDir();
                break;
            case 1:
                moveOffPlatform();
                break;
            case 2:
                slowDown();
                break;
        }
    }

    private void decideXDir() {
        targetXDir = thisController.xDir;
        currentStep = 1;
    }

    private void moveOffPlatform() {
        if (thisController.isGrounded) {
            if (targetXDir == 1) {
                thisController.moveRight();
            } else {
                thisController.moveLeft();
            }
        } else {
            currentStep = 2;
        }
    }

    private void slowDown() {
        if (targetXDir == 1) {
            thisController.moveLeft();
            if (thisController.vel.x <= 0) {
                nextObjective = new AttackObjective(thisPlayer);
            }
        } else {
            thisController.moveRight();
            if (thisController.vel.x >= 0) {
                nextObjective = new AttackObjective(thisPlayer);
            }
        }
    }
}
