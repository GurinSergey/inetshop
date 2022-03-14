package com.webstore.base.stateStepBase;

import com.webstore.base.stateStepBase.baseInterface.IStateStep;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class StateMainHandler<T> {
    @Autowired
    public StepService stepService;

    public  /*IStateStep*/ <V extends IStateStep> V runStateStep(T state) {
        V stateStep;
        stateStep = createStateStep(state);
        if (stateStep == null) return  stateStep;

        if (stateStep.before()) {
            if (stateStep.validate()) {
                if (stateStep.execute()) {
                    stateStep.after();
                }
            }
        }

        return stateStep;
    }

    abstract /*IStateStep*/ <V extends IStateStep> V createStateStep(T state);

    boolean init() {
        return true;
    }

    public StepService getStepService() {
        return stepService;
    }

    public void setStepService(StepService stepService) {
        this.stepService = stepService;
    }
}
