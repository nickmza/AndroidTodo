package mu.mcb.mobileacademytodo

import com.google.firebase.auth.FirebaseAuth
import mu.mcb.mobileacademytodo.Interfaces.IUserInfo

class UserInfo : IUserInfo{
    override fun getUserId(): String {
        var user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            return user.uid
        } else{
            return ""
        }
    }
}