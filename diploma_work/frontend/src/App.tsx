import {Router} from "./Router";
import {useEffect, useState} from "react";
import {getAuth} from "./api";
import {AppLoader} from "./components/AppLoader/AppLoader";
import {User} from "./types";
import {UserContext} from "./context/UserContext";
import {AppLayout} from "./components/Layout/Layout";

export function App() {
    const [isLoadingAuth, setIsLoadingAuth] = useState(true);
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        getAuth().then(
            resp => {
                if (resp.ok) {
                    resp.json().then(user => setUser(user))
                }
            }
        ).finally(() => setIsLoadingAuth(false));
    }, []);

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