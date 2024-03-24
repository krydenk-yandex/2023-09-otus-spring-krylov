import React, {FC} from "react";
import {Col, Form, Row} from "react-bootstrap";

type Props = {
    formData: {title: string, text: string}
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export const ChapterCreateForm: FC<Props> = ({formData, onChange}) => {
    return (
        <Row>
            <Col xs={4}>
                <Form.Group className="mb-3" controlId="id-input">
                    <Form.Label>Заголовок</Form.Label>
                    <Form.Control type="input" value={formData.title} onChange={onChange}
                        name="title" />
                </Form.Group>
            </Col>
            <Col xs={8}>
                <Form.Group className="mb-3" controlId="name-input">
                    <Form.Label>Текст</Form.Label>
                    <Form.Control type="textarea" name="text" as="textarea" rows={3}
                                  value={formData.text} onChange={onChange}/>
                </Form.Group>
            </Col>
        </Row>
    );
}