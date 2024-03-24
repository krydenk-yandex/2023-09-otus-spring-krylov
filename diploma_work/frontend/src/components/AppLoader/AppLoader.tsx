import {Spinner} from "react-bootstrap";
import css from './AppLoader.module.css';

export const AppLoader = () => (
    <div className={css.appLoader}>
        <Spinner />
    </div>
)