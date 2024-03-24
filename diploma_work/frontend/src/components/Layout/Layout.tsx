import { FunctionComponent, PropsWithChildren } from 'react';
import {Header} from "./Header";

import {User} from "../../types";
import {Container} from "react-bootstrap";

type Props = {
    user: User | null
}

export const AppLayout: FunctionComponent<Props & PropsWithChildren> = ({user, children}) => {
    return <>
        <Header user={user}/>
        <Container fluid className="col-8 mt-4">
            {children}
        </Container>
    </>
}