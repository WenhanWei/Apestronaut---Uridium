package customClasses.AIObjectives;

import components.*;
import GameObjects.*;
import java.util.Random;

public class AttackObjective extends Objective {

    GameObject thisPlayer;
    GameObject targetPlayer;
    Transform thisTransform;
    Transform targetTransform;
    AIPlayerController thisController;

    Random rand = new Random();

    int targetXDir = 0;

    public AttackObjective(GameObject thisPl) {
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
                followPlayer();
                break;
        }
        if (thisController.checkPlatformAbove() && targetPlayer.GetComponent(PlayerController.class).isGrounded) {
            if (thisTransform.pos.y > targetTransform.pos.y + 90) {
                nextObjective = new MoveUpObjective(thisPlayer, true);
            } else if (thisTransform.pos.y > targetTransform.pos.y + 50) {
                nextObjective = new MoveUpObjective(thisPlayer, false);
            }
        } else if (targetTransform.pos.y - 50 > thisTransform.pos.y && thisController.isGrounded) {
            nextObjective = new MoveDownObjective(thisPlayer);
        }
    }

    private void followPlayer() {

        //Debug.Log("Can attack");
        if (targetPlayer == null)
            return;

        if (thisController.isGrounded) {
            if (targetTransform.pos.y < thisTransform.pos.y -150)
                thisController.jump();
        } 

        if (Math.abs(targetTransform.pos.x - thisTransform.pos.x) < thisController.kickDist) {
            if (hasWaited(1f + rand.nextFloat()*5f)) {
                if (!thisController.boosters.isEmpty()) {
                    thisController.boosters.pop().useBooster(thisPlayer);
                } else {
                    thisController.kick();
                }
            }
        }

        if (Math.abs(targetTransform.pos.x - thisTransform.pos.x) < thisController.squashDist * 0.2f) {
          //  Debug.Log("AI trying to squash");
        //    thisController.squash(thisPlayer);
        }

        if (targetXDir == 1) {
            thisController.moveRight();
        } else {
            thisController.moveLeft();
        }
        currentStep = 0;
    }

    private void decideXDir() {
        if (targetTransform.pos.x < thisTransform.pos.x) {
            targetXDir = -1;
        } else {
            targetXDir = 1;
        }

        if (thisController.xDir != targetXDir) {
            if(hasWaited(10f)) {
                currentStep = 1;
            } else {
                //Debug.Log("Can't attack");
                if (targetXDir == -1) {
                    thisController.moveRight();
                } else {
                    thisController.moveLeft();
                }
            }
        } else {
            currentStep = 1;
        }
    }
}
