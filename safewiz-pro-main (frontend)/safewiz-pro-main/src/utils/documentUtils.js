export const getTargetId = (writeYear, docNo, docSeq, docDetailSeq) => {

    if(!docNo){
        return null;
    }
    let targetId = writeYear + docNo;

    if (docSeq) {
        targetId += docSeq;
    }

    if (docDetailSeq) {
        targetId += docDetailSeq;
    }

    return targetId;
};

export const getFormattedTargetId = (targetType, writeYear, docNo, docSeq, docDetailSeq, isTask = true) => {

    if (!isTask || !docNo) {
        return null; // isTask가 false이면 null 반환
    }

    let formattedTargetId = `${targetType}^&${writeYear}^&${docNo}`;

    if (docSeq) {
        formattedTargetId += `^&${docSeq}`;
    }

    if (docDetailSeq) {
        formattedTargetId += `^&${docDetailSeq}`;
    }

    return formattedTargetId;
};
