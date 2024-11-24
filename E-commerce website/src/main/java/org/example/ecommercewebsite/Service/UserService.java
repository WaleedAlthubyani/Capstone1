package org.example.ecommercewebsite.Service;

import org.example.ecommercewebsite.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserService {

    HashMap<String, User> users = new HashMap<>();

    public ArrayList<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    public boolean createNewUser(User user){
        if (users.containsKey(user.getId()))
            return false;//fail user already exist

        users.put(user.getId(),user);
        return true;
    }

    public int updateUser(String userId,User user){
        if (!users.containsKey(userId))
            return 0;//fail user not found
        if (!userId.equals(user.getId()))
            return 1;//fail user ID must match

        users.put(userId,user);
        return 2;
    }

    public boolean deleteUser(String userId){
        if (!users.containsKey(userId))
            return false;//fail user not found

        users.remove(userId);
        return true;
    }
}
