import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const rootDir = path.resolve(__dirname, 'src');
const exts = ['.vue', '.js', '.ts'];

// 재귀적으로 파일 리스트 가져오기
function getFiles(dir) {
  let results = [];
  const list = fs.readdirSync(dir);
  list.forEach(file => {
    const filePath = path.join(dir, file);
    const stat = fs.statSync(filePath);
    if (stat.isDirectory()) {
      results = results.concat(getFiles(filePath));
    } else if (exts.includes(path.extname(file))) {
      results.push(filePath);
    }
  });
  return results;
}

// 주석을 제거하는 함수 (한 줄 주석 + 블록 주석 제거)
function removeComments(code) {
  return code
    .replace(/\/\*[\s\S]*?\*\//g, '')   // 블록 주석 제거
    .replace(/\/\/.*$/gm, '');           // 한 줄 주석 제거
}

// import 경로 추출 (주석 제거 후)
function extractImports(content) {
  const code = removeComments(content);
  const importRegex = /import\s+.*?from\s+['"](.*?)['"]/g;
  const imports = [];
  let match;
  while ((match = importRegex.exec(code)) !== null) {
    imports.push(match[1]);
  }
  return imports;
}

// 경로 대소문자 검사 (생략: 기존과 동일)
function checkPathCase(importPath, baseDir) {
  if (importPath.startsWith('@/')) {
    importPath = path.join(rootDir, importPath.slice(2));
  } else if (importPath.startsWith('.')) {
    importPath = path.resolve(baseDir, importPath);
  } else {
    return null;
  }

  const tryExts = ['', '.vue', '.js', '.ts'];
  let realPath = null;
  for (const ext of tryExts) {
    const p = importPath + ext;
    if (fs.existsSync(p)) {
      realPath = p;
      break;
    }
  }
  if (!realPath) return {error: '파일 없음', path: importPath};

  const parts = path.relative(rootDir, realPath).split(path.sep);
  let currentPath = rootDir;
  for (const part of parts) {
    const dirContents = fs.readdirSync(currentPath);
    const matched = dirContents.find(f => f === part);
    if (!matched) {
      const altMatch = dirContents.find(f => f.toLowerCase() === part.toLowerCase());
      if (altMatch) {
        return {
          error: '대소문자 불일치',
          expected: altMatch,
          actual: part,
          fullPath: realPath,
        };
      } else {
        return {error: '경로 없음', part, currentPath};
      }
    }
    currentPath = path.join(currentPath, part);
  }
  return null;
}

// 메인 실행
function main() {
  const files = getFiles(rootDir);
  let issues = [];
  for (const file of files) {
    const content = fs.readFileSync(file, 'utf8');
    const baseDir = path.dirname(file);
    const imports = extractImports(content);
    for (const imp of imports) {
      const res = checkPathCase(imp, baseDir);
      if (res && res.error === '대소문자 불일치') {
        issues.push({
          file,
          importPath: imp,
          expected: res.expected,
          actual: res.actual,
        });
      } else if (res && res.error === '파일 없음') {
        issues.push({
          file,
          importPath: imp,
          issue: '파일이 존재하지 않음',
          checkedPath: res.path,
        });
      }
    }
  }

  if (issues.length === 0) {
    console.log('대소문자 불일치 및 경로 문제 없음');
  } else {
    console.log('발견된 문제:');
    issues.forEach(({file, importPath, expected, actual, issue, checkedPath}, i) => {
      console.log(`\n[${i+1}] 파일: ${file}`);
      console.log(`  import 경로: ${importPath}`);
      if (issue) {
        console.log(`  문제: ${issue}`);
        console.log(`  검사 경로: ${checkedPath}`);
      } else {
        console.log(`  대소문자 불일치 - 실제: ${expected}, import에 적힌 것: ${actual}`);
      }
    });
  }
}

main();
