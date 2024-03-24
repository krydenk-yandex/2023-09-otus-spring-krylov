import {FC, useCallback, useState} from "react";
import {Button, Col, Row} from "react-bootstrap";
import {ModalChaptersList} from "../СhaptersList/ModalChaptersList";
import {Chapter} from "../../types";
import {useNavigate} from "react-router-dom";

type Props = {
    chapter: Chapter;
}

export const ChapterHeader: FC<Props> = ({chapter}) => {
    const [isModalOpen, setModalOpen] = useState(false);
    const navigate = useNavigate();

    const toNext = useCallback(
        () => navigate(`/chapters/${chapter?.nextChapter?.uuid}`),
        [chapter?.nextChapter?.uuid, navigate]
    );

    const toPrev = useCallback(
        () => navigate(`/chapters/${chapter?.prevChapter?.uuid}`),
        [chapter?.prevChapter?.uuid, navigate]
    );

    return <Row className='align-items-center'>
        <Col xs={4} className='d-flex flex-column'>
            <span className='fw-bold fs-5'>{chapter.title}</span>
            <span>{chapter.book.title}</span>
        </Col>
        <Col xs={8} className={'d-flex justify-content-end'}>
            <Row>
                <Col>
                    <Button
                        variant='outline-dark'
                        size='sm'
                        onClick={toPrev}
                        disabled={!Boolean(chapter?.prevChapter)}
                    >
                        ◀&nbsp;
                    </Button>
                </Col>
                <Col>
                    <Button
                        variant='outline-dark'
                        size='sm'
                        onClick={toNext}
                        disabled={!Boolean(chapter?.nextChapter)}
                    >
                        &nbsp;▶
                    </Button>
                </Col>
                <Col>
                    <Button variant='outline-dark' size='sm' onClick={() => setModalOpen(true)}>
                        Содержание
                    </Button>
                    <ModalChaptersList bookId={chapter.book.id}
                        isShown={isModalOpen} onHide={() => setModalOpen(false)} />
                </Col>
            </Row>
        </Col>
    </Row>
}