export default function FemsUtils(){


    //limhs11
    const deepMerge = (target, source) => {
        for (const key in source) {
            if (source[key] instanceof Object && !Array.isArray(source[key])) {
                if (!target[key]) target[key] = {};
                deepMerge(target[key], source[key]);
            } else {
                target[key] = source[key];
            }
        }
        return target;
    };

    const transformArray = arr => {
        return arr.map(item => {
            return {
                value: item.minorCd,
                text: item.minorNm
            }
        })
    }

    /** ✏️ return */
    return {
        deepMerge, transformArray
    }
}
