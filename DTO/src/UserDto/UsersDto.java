package UserDto;

import user.User;

import java.util.HashMap;
import java.util.Map;

public class UsersDto {
    private final Map<String, User> usersList ;

    public UsersDto(Map<String,User> currMap)
    {
        this.usersList = currMap ;
    }

    public UsersDto() {
        usersList = new HashMap<>();
    }

    public Map<String, User> getUsersList() {
        return usersList;
    }

}
