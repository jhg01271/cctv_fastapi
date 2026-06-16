/**
 * ============================================================================================================================================================
 * 👉 EChart 컴포넌트 코어
 * ============================================================================================================================================================
 */
import 'echarts'
import * as echarts from 'echarts/core'

// default theme
import 'echarts/theme/azul'
import 'echarts/theme/bee-inspired'
import 'echarts/theme/blue'
import 'echarts/theme/caravan'
import 'echarts/theme/carp'
import 'echarts/theme/cool'
import 'echarts/theme/dark-blue'
import 'echarts/theme/dark-bold'
import 'echarts/theme/dark-digerati'
import 'echarts/theme/dark-fresh-cut'
import 'echarts/theme/dark-mushroom'
import 'echarts/theme/eduardo'
import 'echarts/theme/forest'
import 'echarts/theme/fresh-cut'
import 'echarts/theme/gray'
import 'echarts/theme/green'
import 'echarts/theme/helianthus'
import 'echarts/theme/infographic'
import 'echarts/theme/inspired'
import 'echarts/theme/jazz'
import 'echarts/theme/london'
import 'echarts/theme/macarons'
import 'echarts/theme/macarons2'
import 'echarts/theme/mint'
import 'echarts/theme/red'
import 'echarts/theme/red-velvet'
import 'echarts/theme/roma'
import 'echarts/theme/royal'
import 'echarts/theme/sakura'
import 'echarts/theme/shine'
import 'echarts/theme/tech-blue'
import 'echarts/theme/vintage'

// Map Data
// import koreaMap from '@/components/echart/map/korea.json'
// import chinaMap from '@/components/echart/map/china.json'
// import worldMap from '@/components/echart/map/world.json'

// const { registerMap, registerTheme } = echarts
// registering map data
// registerMap('korea', koreaMap)
// registerMap('china', chinaMap)
// registerMap('world', worldMap)


// const getThemeUrl = (name) => {
//   return new URL(`./thtme/${name}.json`, import.meta.url).href
// }

// registering theme, 수동 선택
// registerTheme('light01', getThemeUrl('light01'))
// registerTheme('light02', getThemeUrl('light02'))
// registerTheme('light03', getThemeUrl('light03'))
// registerTheme('dark01', getThemeUrl('dark01'))
// registerTheme('dark02', getThemeUrl('dark02'))
// registerTheme('dark03', getThemeUrl('dark03'))
// registerTheme('dark04', getThemeUrl('dark04'))
// registerTheme('dark05', getThemeUrl('dark05'))
// registerTheme('dark06', getThemeUrl('dark06'))
// registerTheme('dark07', getThemeUrl('dark07'))
// registerTheme('dark08', getThemeUrl('dark08'))
// registerTheme('dark09', getThemeUrl('dark09'))
// registerTheme('dark10', getThemeUrl('dark10'))
// registerTheme('dark11', getThemeUrl('dark11'))
// registerTheme('dark12', getThemeUrl('dark12'))
// registerTheme('dark13', getThemeUrl('dark13'))

// // registering default theme (light & dark), Vuetify 테마와 연계 자동 선택
// registerTheme('light', getThemeUrl('light01'))
// registerTheme('dark', getThemeUrl('dark04'))

export default echarts