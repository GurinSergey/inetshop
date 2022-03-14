package com.webstore.base.stateStepBase.baseInterface;

import com.webstore.domain.User;

public interface IStateStep <T,V,P>{
     boolean init(T content,V state, User user,P protocol);
     boolean validate(String...arrParam);
     boolean before(String...arrParam);
     boolean execute(String...arrParam);
     boolean after(String...arrParam);
     boolean rollback(String...arrParam);
}
