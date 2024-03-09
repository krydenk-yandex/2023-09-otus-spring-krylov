import { FunctionComponent, PropsWithChildren } from 'react';
import {Header} from "./Header";

import css from "./Layout.module.css";
import {User} from "../../types";

type Props = {
    user: User | null
}

export const AppLayout: FunctionComponent<Props & PropsWithChildren> = ({user, children}) => {
    return <>
        <Header user={user} />
        <div className={css.appLayoutBody}>
            {children}
        </div>
    </>
}