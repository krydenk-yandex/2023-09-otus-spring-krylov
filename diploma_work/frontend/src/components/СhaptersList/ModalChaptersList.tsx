import {FC} from "react";
import {Modal} from "react-bootstrap";
import {ChaptersList} from "./ChaptersList";

type Props = {
    bookId: number;
    isShown: boolean;
    onHide: () => void;
}

export const ModalChaptersList: FC<Props> = ({bookId, isShown, onHide}) => {
    return <Modal
        show={isShown}
        onHide={onHide}
    >
        <Modal.Header closeButton>
            <Modal.Title>Содержание</Modal.Title>
        </Modal.Header>

        <Modal.Body>
            <ChaptersList bookId={bookId} />
        </Modal.Body>
    </Modal>
}