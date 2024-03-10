import css from './Header.module.css';
import {FC} from "react";
import {User} from "../../types";

type Props = {
    user: User | null
}

export const Header: FC<Props> = ({user}) => {
    return (
        <div className={css.appHeader}>
            <div className={css.appHeaderLeft} onClick={() => {
                // eslint-disable-next-line no-restricted-globals
                location.href = "/";
            }}>
                <img
                    className={css.appHeaderImage}
                    src={'/logo.png'}
                    alt={"LOGO"}
                />
                <span className={css.appHeaderCaption}>
                    Turbo<span style={{color: 'red'}}>Lib</span>
                </span>
            </div>
            {user && (
                <div className={css.appHeaderRight}>
                    <div className={css.appHeaderRightCol}>
                        <span className={css.appHeaderUserName}>{user.username}</span>
                        <span className={css.appHeaderUserAuthorities}>{user.authorities.join(", ")}</span>
                    </div>
                    <img
                        className={css.appHeaderUserImage}
                        src={'/user.png'}
                        alt={"LOGO"}
                    />
                </div>
            )}
        </div>
    );
}