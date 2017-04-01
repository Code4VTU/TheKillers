package com.radoslav.testmafia;

/**
 * Created by Rado on 01/04/2017.
 */

public class GetRoleText {

    public  GetRoleText()
    {

    }

    public String getRole(int role){
        if(role == 0 || role == 1) {
            return "Killer";
        }
        if(role == 2) {
            return "Doctor";
        }
        if (role == 3) {
            return "Policeman";
        }
        if(role == 4 || role == 5)
        {
            return "Ordinary man";
        }
        return "not defined";
    }

}
