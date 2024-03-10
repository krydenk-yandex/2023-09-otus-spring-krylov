import {FC, useEffect, useState} from "react";
import {getChaptersByBookId} from "../../api";
import {Link} from "react-router-dom";
import {Container, Row} from "react-bootstrap";
import {AppLoader} from "../AppLoader/AppLoader";

type Props = {
    bookId: number;
}

export const ChaptersList: FC<Props> = ({bookId}) => {
    const [chapters, setChapters] = useState<{uuid: string, title: string}[] | null>(null);

    useEffect(() => {
        getChaptersByBookId(bookId).then(response => {
            setChapters(response);
        })
    }, [bookId]);

    return chapters ?
        (
            <Container>
                {chapters.map(c => <Row>
                    <Link to={`/chapters/${c.uuid}`}>{c.title}</Link>
                </Row>)}
            </Container>
        ) : <AppLoader />
}