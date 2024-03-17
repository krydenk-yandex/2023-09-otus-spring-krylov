import {Router} from "./Router";
import {useCallback, useEffect, useState} from "react";
import {getAuth} from "./api";
import {AppLoader} from "./components/AppLoader/AppLoader";
import {User} from "./types";
import {UserContext} from "./context/UserContext";
import {AppLayout} from "./components/Layout/Layout";
import {ACCESS_TOKEN_KEY} from "./utils";

export function App() {
    const [isLoadingAuth, setIsLoadingAuth] = useState(true);
    const [user, setUser] = useState<User | null>(null);

    const dropAuthorization = useCallback(() => localStorage.removeItem(ACCESS_TOKEN_KEY), []);

    useEffect(() => {
        getAuth().then(
            resp => {
                if (resp.ok) {
                    resp.json().then(user => setUser(user))
                } else {
                    dropAuthorization()
                }
            }
        )
        .catch(_ => dropAuthorization())
        .finally(() => setIsLoadingAuth(false));
    }, [dropAuthorization]);

    return <AppLayout user={user}>
        {
            isLoadingAuth
                ? <AppLoader />
                : <UserContext.Provider value={user}>
                    <Router user={user} />
                </UserContext.Provider>
        }
    </AppLayout>
}