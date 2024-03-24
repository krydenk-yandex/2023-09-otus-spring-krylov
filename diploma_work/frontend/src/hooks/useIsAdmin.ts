import {useContext} from "react";
import {UserContext} from "../context/UserContext";

export const useIsAdmin = () => {
    const user = useContext(UserContext);
    return user?.authorities.indexOf("ROLE_ADMIN") !== -1;
}