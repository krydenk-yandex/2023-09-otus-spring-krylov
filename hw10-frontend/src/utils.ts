import React from "react";

export const toFormData = (object: Object) => {
    return Object.entries(object).reduce((d,e) => (d.append(...e), d), new FormData())
}

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