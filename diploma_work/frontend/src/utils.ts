import React from "react";
import {Book} from "./types";

export const ACCESS_TOKEN_KEY = "accessToken";


export const getSelectInputValue = (e: React.ChangeEvent<HTMLSelectElement>) => {
    let value: string | string[];

    if (e.target.multiple) {
        value = [];
        for (let i = 0; i < e.target.selectedOptions.length; i++){
            value.push(e.target.selectedOptions.item(i)!!.value)
        }
    } else {
        value = e.target.value;
    }

    return value;
}

export const MINIO_URL = 'http://localhost:9000/library';

export const getBookCover = (book: Book) => book.coverUrl ? `${MINIO_URL}${book.coverUrl}` : '/logo.png';

export function fileToBase64(file: File) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}