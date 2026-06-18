import customKo from './ko/custom.js'
import customEn from './en/custom.js'

import systemKo from './ko/system.js'
import systemEn from './en/system.js'

//중복의 경우, 후속 객체가 덮어씌움
const messages = {
    ko: { ...systemKo, ...customKo },
    en :{ ...systemEn, ...customEn },
}
export default messages