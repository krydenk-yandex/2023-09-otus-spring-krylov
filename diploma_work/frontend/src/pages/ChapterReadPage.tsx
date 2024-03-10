import React, {FC, useEffect, useMemo, useState} from 'react';
import {Chapter} from "../types";
import {useParams} from "react-router-dom";
import {getChapterByUuid} from "../api";
import {ChapterHeader} from "../components/ChapterHeader/ChapterHeader";
import {AppLoader} from "../components/AppLoader/AppLoader";
import {Col, Container, Row} from "react-bootstrap";


export const ChapterReadPage: FC = () => {
    let { uuid } = useParams();
    const [chapter, setChapter] = useState<Chapter>();

    useEffect(() => {
        Promise.all([
            getChapterByUuid(uuid!!),
        ]).then(([chapter]) => {
            setChapter(chapter);
        })
    }, [uuid])

    const header = useMemo(() => (
        chapter
            ? <ChapterHeader chapter={chapter} />
            : null
    ), [chapter]);


    return chapter ? (
        <Container>
            {header}
            <Row className='mb-4 mt-4 col row'>
               <Col>
                   <pre style={{whiteSpace: "pre-wrap"}}>
                       {chapter.text}
                   </pre>
               </Col>
            </Row>
            {header}
        </Container>
    ) : <AppLoader/>
}
