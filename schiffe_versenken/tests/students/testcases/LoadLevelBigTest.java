package students.testcases;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import adapters.BattleshipTestAdapterMinimal;

public class LoadLevelBigTest {
	
	
	String[] LEVEL_CORRECT_ARRAY =  {
			"---t--t---|----------\n-t-v--v---|-----lhr--\n-b-b--b-lr|----------\n----------|lr----lhr-\n-lr--lhhr-|--lhhr----\n----t-----|t------t-t\n-t--v-lhhr|v--t---b-v\n-v--v-----|b--b-----v\n-b--v--t--|------lr-v\n----b--b--|--lhhr---b", 
			"----------|----------\n--lr------|-lhr------\n-t-----lhr|----------\n-v-lr-----|--lhr----t\n-b---lhhr-|-------t-b\n----------|-lhhhr-v--\n---lhhr-lr|-------v-t\n-lr-------|-lhr---b-b\n------lhr-|----lr----\n-lhhhr----|-lr---lhhr", 
			"--lr--lhhr|------lhhr\n----------|--lhr-----\n----------|--------lr\n---lhr----|-lhr------\n-t------lr|-------t--\n-v-lhr----|-t-----b--\n-b------t-|-b-lr-----\n---lhhr-b-|-----lhhr-\n----------|lhhhr-----\n-lr-lhhhr-|-------lhr", 
			"---lhhr---|-------lhr\n-t--------|-t--------\n-b---lhhr-|-v----lr--\n---lr-----|-v---t---t\n-------lr-|-b---v-t-v\n-lhhhr----|---t-v-b-v\n--------t-|-t-v-v---b\nt--lhr--v-|-b-b-b--t-\nv-------b-|--------v-\nb-----lr--|----lr--b-", 
			"--t-------|-----lhhr-\nt-b---lr--|--lr------\nv---------|----------\nb-lhhr-lhr|t------lr-\n-t--------|v--t--t---\n-v-t---t--|b--b--v-lr\n-b-v---v--|-t--t-v---\n---v---v--|-v--v-v-t-\n---b---v--|-v--b-b-v-\nlr--lr-b--|-b------b-", 
			"------lhhr|-----lhhr-\n-t---t----|-t--------\n-b---b--t-|-v-lr-lhr-\n--lhr-t-v-|-v-------t\n------v-v-|-b-lr----v\n-lhhr-b-v-|-----t---b\n--------b-|-----v-lr-\nt---t-----|--t--v----\nb---v-----|--b--v-lhr\n----b---lr|-----b----", 
			"--lhr-----|-lhr--lhr-\n-----lr--t|----------\n---------v|-lhhr---lr\nt-t------b|-------t--\nv-v-t-----|-t-----v--\nv-v-v-lr--|-v-t---v--\nv-b-b-----|-b-v-t-b-t\nb------lr-|---v-b---b\n-t-lhhr---|---v------\n-b--------|---b----lr", 
			"t----lhr--|----lr-t--\nb--lr-----|-------b--\n-t--------|--lhhr--t-\n-b----lhhr|------t-v-\n----------|t-lhr-b-b-\n-t-------t|v---------\n-v--lhhr-v|b--lhhhr--\n-b-------v|--t-------\n-------t-v|--b--lhhr-\n-lhr---b-b|----------", 
			"---lhhr---|---lhhhr--\n----------|-lr-----t-\n-t--lr--lr|--------v-\n-b-t------|---lhhr-v-\nt--v-----t|--------b-\nv--b---t-v|-lr-lhr---\nv------v-v|t--t------\nv------b-b|v--v--t-t-\nb---------|b--b--b-b-\n--lhr--lr-|----------", 
			"---lr--lhr|----------\nlhr--lr---|lhr-lhhhr-\n----------|---t------\n---------t|-t-v---lhr\n-lhhr-t--b|-b-v------\n------b---|---b--t-t-\n-lhhr-----|-lr---v-v-\n-----lhr--|---lr-v-b-\n----------|------b--t\n---lhhhr--|---------b", 
			"-lhhhr----|----------\n------t---|----lhr-lr\nlhr-t-v---|--lr------\n----b-b---|----t-----\n--t------t|----v-lr--\nt-b------v|lhr-b-----\nv-----lr-v|---t-lhhr-\nb--------b|---v------\n-lhhr---t-|-t-v-lhhhr\n--------b-|-b-b------", 
			"------lhhr|------lhhr\n---lr-----|-t-lhr----\n------lhr-|-v--------\n-lhr------|-v--lhr-lr\n------lr--|-b--------\nt-------t-|-----lhr-t\nb-lhr---v-|---------b\n-----lr-v-|------lr--\n-lhhr---v-|-lhhhr--lr\n--------b-|----------", 
			"-lhr--t---|----------\n------b---|--lhhhr---\nlr------t-|-t------t-\n---lhhr-b-|-b----t-v-\n-t--------|t---t-v-b-\n-v----t---|b-t-v-b---\n-v--t-v-t-|--b-v-----\n-v--b-b-v-|----b---t-\n-b------v-|-----lr-v-\n---lhr--b-|lhhr----b-", 
			"t---lhr---|----------\nv---------|-------lr-\nv-t--lr--t|-t--------\nv-v------b|-v-lhr--lr\nb-b--lhhr-|-b--------\n---t------|t-t-t-----\n---b------|v-v-v-t---\n-t----lhhr|v-v-b-b---\n-v-lr-----|b-b-----lr\n-b--------|---lhhhr--", 
			"t--lr--lhr|-----lr-lr\nv----lr---|-lhhr--t--\nv-t----lhr|-------b--\nb-v-------|--t-lhr---\n--v-t-----|--b------t\n--b-v---lr|-t--t--t-v\n----b-----|-v--v--v-v\n----------|-v--b--b-v\nlhhhr--t--|-b-------b\n-------b--|----------", 
			"--t--lhr--|--------lr\n--b------t|-lhhr-----\n-------t-v|--------t-\n----t--v-v|-lhr--t-b-\n-t--b--v-b|------v--t\n-v-t---v--|---lr-v--v\n-b-v---b--|------b--v\n---b------|-t-------v\n------t-lr|-b-lhr---b\n-lhhr-b---|------lhr-", 
			"----lhhr-t|--t-------\n---------b|--v----lhr\n---lhr----|--v-t-----\n-lr-----t-|--b-b---t-\nt-------b-|t--t--t-b-\nv--t-lhr--|v--v--b--t\nv--v------|b--b-----v\nb--v-t----|---------v\n---v-v-lr-|--lhhr---v\n---b-b----|------lr-b", 
			"----t---lr|---lhr----\n-t--v--t--|--t-------\n-v--v--v--|--v----t-t\n-v--b--v-t|--b-t--v-v\n-b-----v-b|----v--v-b\n----t--b--|----v--b--\n-t--b----t|-lr-b-----\n-v-t-----v|t-------lr\n-b-v-----b|b--lr-----\n---b--lr--|-----lhhhr", 
			"------t-lr|----t---t-\nt--t--v---|-t--v---b-\nv--v--b---|-v--b--t-t\nv--v------|-b-t---v-v\nv--b--lhhr|---b---v-v\nb---------|-------v-b\n--lhr----t|t------b--\n-----lr--b|b--lhhr---\n--t-------|--t-------\n--b-lhr---|--b---lhr-", 
			"-t--------|-----lhhr-\n-v-lhhr-t-|-t--------\n-v------v-|-v---lhr--\n-v--t---b-|-v-lr---t-\n-b--v-lr--|-v------b-\n----b-----|-b--------\nt--------t|--t------t\nb-lhhr---b|--v---lr-v\n-------lr-|--v-lr---b\n----lhr---|--b---lhr-", 
			"----------|-lhr------\n-lr-t-----|t----lhhhr\n----v-lhr-|v---------\n-t--v-----|v----lr---\n-v--b--lhr|b---------\n-v--------|----t---lr\n-b------t-|-lr-v-----\n--lhhhr-b-|----b--lhr\nt---------|----------\nb-lr--lhr-|--lhhr--lr", 
			"-----lhr--|t--lhhhr--\nt---t----t|v---------\nb---b----v|b-lhr-----\n--t--lhr-v|------lhhr\n--b------b|lr-t------\n-t--------|---b------\n-v--lhhhr-|lhr-lhhr--\n-v-------t|----------\n-b---t---v|---t------\n-----b---b|---b----lr", 
			"-----lr---|----------\n--t-t-----|------lhr-\n--v-b-lhr-|----lr----\n--b-------|-lr----t-t\n---lr-----|----lr-v-v\nt---------|--t----b-v\nv-----lr--|--v-t----b\nv-lhhr----|t-v-b-----\nv---------|v-b--lhhhr\nb-lhhr-lhr|b---------", 
			"-lr---lr--|------lr--\n---------t|--lr-t----\n--lhhr---b|-----v-lr-\nt------t--|-----v----\nv------v--|-----v-t--\nb---t--b--|lhhr-b-v--\n----v-t-t-|-------b--\n----v-v-v-|lhr---t---\n-t--v-v-b-|------b---\n-b--b-b---|-lhhr--lhr", 
			"-t-lr-----|------lhhr\n-v-------t|-lhhhr----\n-v--lhhr-b|-------t-t\n-b--------|-t---t-b-v\n-------t--|-b---v---b\n-t-lr--b--|--lr-v----\n-v--------|-----b----\n-v-t-t-lhr|--------t-\n-v-v-v----|-lhr----b-\n-b-b-b----|-----lhr--", 
			"----------|--lhr--lr-\n-lr--lhhhr|lr--------\n----------|------t---\nt-----t---|------b--t\nv-t-t-v-lr|-lhhhr---v\nv-v-b-v---|------t--b\nb-b---b-t-|------v-t-\n--------v-|lhhr--b-v-\nlhr---t-b-|----t---v-\n------b---|----b---b-", 
			"----------|t---------\nt-lhhhr---|v-t-lhhhr-\nv------lhr|b-b-------\nb---------|---lr--lhr\n--t-lhhr--|----------\n--b-------|lhhr--t-t-\nt------lr-|------v-v-\nv---t-----|------b-v-\nb-t-b-lhhr|lr--lr--b-\n--b-------|----------", 
			"--t--lhr--|--------t-\nt-b-------|-lhhhr--v-\nv--t--lhhr|--------v-\nv--v------|----lhr-b-\nv--b-lr---|----------\nb-t-------|-t-lhr--t-\n--b--t-t--|-b----t-b-\n-----v-v--|------b--t\nlr---v-b--|---------v\n-----b----|-lhhr-lr-b", 
			"-lhhr-t---|-t------lr\n------b---|-v-t-t-t--\n-------lhr|-v-v-v-b--\nt---t-----|-b-b-b----\nv---b-lhhr|t---------\nb-t-------|v---lhhr--\n--v-------|v--t----lr\nt-b---lr--|v--v--lr--\nb---------|b--b------\n-----lhhhr|----------", 
			"---------t|-lr-------\n---lhr---v|---lhr--lr\n-lr------b|----------\n----lr----|-t--lhhhr-\nt-----lhhr|-b--------\nv---------|-------t-t\nv---lhhr--|---t---v-v\nv-t-----t-|-t-v---b-b\nb-b-lr--v-|-b-v------\n--------b-|---b-lhhr-", 
			"-t-----lhr|t-t-----t-\n-b--------|v-b--t--v-\n--lhr-lhhr|b----v--b-\n----------|-t---v-t--\n--lhhr-lr-|-v---b-b--\n-t--------|-v--t-----\n-v--------|-v--b--lhr\n-v--lhr-t-|-b--------\n-v-t----b-|--------lr\n-b-b------|----lhhr--", 
			"--t-t-----|---------t\n--v-b-----|t-lhhr---v\nt-v--lhhr-|v--------v\nb-v-------|v---t-lr-v\n--b----lhr|b---b----b\n---lr-----|--t---lr--\nt-t-----lr|--v-------\nv-v-lhr---|--b--lhr--\nb-v-------|lr--------\n--b-------|------lhr-", 
			"---------t|-lr-------\n-lr-lhhr-b|----------\n----------|---lr-lhr-\nlhhhr----t|--t-------\n-----lr--v|--v--lhhr-\nt------t-b|t-v-------\nv--t---b--|b-b-lr-lhr\nv--v------|----------\nb--b---lhr|lhhhr-----\n----------|-----lhr--", 
			"----------|---lhhhr--\n--lhhr---t|lr--------\n-t-------v|----lhr-t-\n-b-t-----b|-lhr----v-\nt--v--lr--|--------v-\nv--v------|--lr----b-\nb--b-lhr-t|t----lhr--\n---------b|b--t------\n-lhhhr-lr-|---b-lhhr-\n----------|----------", 
			"-lhhhr----|------lhhr\n----------|---lr-----\n--t-lhhr--|--------t-\n--v-----lr|-lhhhr--v-\n--v--lhr--|--------b-\n--b-----lr|---lr--t--\n------t---|-t---t-b--\n----t-v---|-v-t-v--t-\nlhr-b-b---|-v-b-b--v-\n--------lr|-b------b-", 
			"------lhhr|---lr-t---\nlhr-lr----|-t----v--t\n----------|-v-t--v--v\n--lhr-----|-v-v--b--v\n-t----lhr-|-v-b-----b\n-v-t------|-b--------\n-v-b--lr--|-----lhr--\n-v--------|t-------t-\n-b-----t--|b-----t-b-\n--lhhr-b--|--lhr-b---", 
			"---lr-t---|lhr-------\n-t----b---|---t-lhhhr\n-v-t-t----|---b------\n-b-v-v---t|t----lhr--\n---b-v-t-v|v-t------t\n-----v-b-v|v-b-lhhr-b\n--t--b---b|b---------\n--v----t--|---lhr----\n--v-lr-v--|--------t-\n--b----b--|--------b-", 
			"----------|lhr-lhhr-t\n----lhr-t-|---------b\n--------b-|-----t-t--\n--lhhr----|-lhr-v-v--\n-t----lhr-|-----b-v--\n-b-t------|-lr----b--\n---b-lhhr-|-----lr---\n-t--------|---lr-----\n-v-lhhhr--|----------\n-b------lr|---lhhhr--", 
			"t---------|---lhr----\nb-lhhhr---|---------t\n-t------lr|-t----t--v\n-v--------|-b--t-v--b\n-b-lhhr---|----v-v---\n----------|--t-v-v-t-\n--lhr-----|--b-b-b-v-\n-----lhhr-|--------b-\n--t-t-----|lr--lhhr--\n--b-b-lhr-|--lr------", 
			"----------|----------\n----lr----|-lhr-lr-t-\n-t----lhr-|--------b-\n-v--lr----|-lhr------\n-b------lr|t----t-t--\nt---lhr---|v----v-v--\nv--t---t--|v----b-v-t\nv--v-t-v--|b------b-b\nv--v-b-v--|---lr-----\nb--b---b--|-----lhhhr", 
			"---lhr-lr-|lr--------\n----------|----------\nlhhr---t--|--lhhr----\n-------v--|-------t-t\n-lhr---v--|--t-t--v-v\n-------b--|t-v-v--b-b\nlhhhr-----|b-b-v-t---\n------lhr-|----v-v---\n---lr-----|--t-b-v--t\n-lr--lr---|--b---b--b", 
			"----lhhhr-|--t-------\n--lr-----t|--v-lhr--t\n-----lhr-v|--b------v\n---------b|t-----t--v\n-t----t---|b-t---v--b\n-v-lr-v---|--b---v---\n-v----b-lr|------b--t\n-b--------|-t-lr----v\n-----lr---|-b-------b\n-lhhr-----|---lhhhr--", 
			"lr--------|t-lr------\n----lhr---|b--------t\n-lr-------|-t---lhr-v\nt----lhr--|-v-------v\nv---------|-v--lhhr-v\nv-lhhr-t--|-b-------b\nb------b--|-----lhr--\n-lhr------|---t------\nt----lhhhr|---b------\nb---------|-lr----lhr", 
			"------lr--|----t--t--\nt-------lr|t-t-v--v-t\nv-lhr-----|v-v-b--b-v\nv---------|v-v------v\nb-t---lhhr|b-v---lr-b\n--v-lr----|--b-------\n--v---lhr-|----t-t--t\n--v-------|----b-v--b\nt-b---lhr-|------b---\nb---------|-lr-------", 
			"--lr------|----------\n-t--lr----|--lhr-----\n-v----lhr-|t---------\n-b-------t|b-----lhr-\n---lr-lr-v|-lhhr-----\n--t------v|-------t-t\n--v------b|-t-lhr-v-v\n--v--lhhr-|-b-----v-v\n--v-------|--t-lr-v-b\n--b---lhr-|--b----b--", 
			"------lr--|-lr-------\n----lr----|-----lhr--\n----------|----------\n-----lhhr-|-t-t------\n-lhr------|-v-v-lhhhr\nt---lr----|-v-v------\nv--t--t--t|-b-b---t-t\nv--v--b--v|t------v-v\nv--b-----b|b-t----b-b\nb----lhhr-|--b-lr----", 
			"---lhr----|-t-----t-t\nt-------t-|-b-----b-v\nv--t--t-v-|t--lhr---v\nb--b--v-v-|v-----t--v\n--t-t-v-b-|v-----v--b\nt-b-b-b---|b-lhr-b---\nb--t------|-----t-t--\n---v------|--lr-b-v--\n---b-lhhhr|-------v--\n----------|-------b--", 
			"----lhr---|---lhr----\n----------|-------lhr\n--t-----lr|----lr----\nt-v-lhr---|------lhhr\nv-v-------|lr-t-t----\nv-v----t-t|---v-v-lhr\nb-b----b-b|---v-v----\n------t---|---v-b----\n------v---|lr-b---t--\n-lhhr-b-lr|-------b--", 
			"-t--------|--------lr\n-b-t-lr---|--lr------\n---v------|----lr--t-\n-t-v-lhr-t|--------v-\n-v-v-----v|--t-t-t-b-\n-b-b--lr-v|--b-v-v--t\n----t----b|t---b-v--v\n----b-----|v-----v--b\n------lhr-|v-----b---\n-lhhr-----|b-lhhr----", 
			"------lr--|----lr--t-\n--lhhr----|--------v-\n-t-------t|-t------b-\n-b--lhhr-v|-b--lhr--t\n---------v|---t-----v\n-lhr-lr--v|t--b-t-t-v\n---------b|v----b-v-v\n----------|v-lhr--v-b\n--t----lhr|b------b--\n--b-lhr---|----------", 
			"---lr---t-|-lr--lr---\n------t-v-|t--lr-----\n---lr-b-b-|v---------\nlhr-------|b---lhhr--\n----------|--lr------\nt---lhhhr-|-----lhhhr\nv-t-------|lhr-------\nv-b---lhr-|----------\nb---------|---lhr----\n-lhhr-----|------lhhr", 
			"-----lhhhr|-lhhr-----\n-lr-t-----|------t---\nt---b-----|lhhr--b---\nv----t-t--|----------\nv-t--b-v--|t-lhr-t---\nb-v----v--|v-----v---\n--b--t-b--|v--t--b---\n---t-b--t-|v--b----t-\n---v----v-|b-t-----b-\n---b----b-|--b-lhr---", 
			"----lhr---|-------lhr\n---t------|----------\nt--v-lr-lr|--lhr-t-lr\nv--b------|------b---\nv-t-------|t--lhr----\nb-v--lr-t-|b-t-------\n--v-----v-|--v-------\nt-v-----v-|--v--lhhr-\nv-b-----b-|--v-t-----\nb---lr----|--b-b-lhhr", 
			"----------|-----lhr--\nlhhhr-lhr-|-lhhr-----\n----------|--------lr\n-t----t---|-t-t--lr--\n-v----b--t|-b-v----t-\n-b-t-----b|t--v----b-\n---b-lhhr-|v--v--t---\nt--------t|v--b--v---\nb--lhhr--v|b-----b---\n---------b|-lhr------", 
			"-lr-------|lhhr-----t\n---lr-lr--|-----lr--b\n----------|--lhr-----\n-----t-t--|------lhr-\nlhhr-v-v--|--lhhr----\n-----v-b-t|----------\n-t---b---v|-lhhhr-t--\n-v-------b|-------b--\n-b-lr-----|----------\n-----lhhhr|-lhr--lr--", 
			"--lhhr----|----------\nt------t--|------lhhr\nb-t--t-v--|t-lhr-----\n--v--b-v--|v-----lr-t\n--b----b--|b----t---v\nt---------|-t---b---v\nv---lr-lhr|-v-------b\nb---------|-v-lhr----\n-lhhhr-lr-|-v----t---\n----------|-b----b-lr", 
			"------t-t-|----------\n--lhr-b-v-|--t---lhr-\n-----t--b-|t-b-------\n-lhr-b----|v-------lr\n----t--lr-|v-lhhr-t--\nt---v-t---|v------v--\nv---v-v---|b-t----b-t\nv---b-v-lr|--b------v\nb-----v---|-t--lhr--v\n------b---|-b-------b", 
			"-t-t------|t-----lr--\n-v-v-t----|v-t-t-----\n-v-v-v--lr|v-v-b-----\n-b-b-v-t--|b-b---lhr-\n-----v-v--|----------\n--t--b-b--|-lhhr---t-\nt-b-t-----|--------b-\nv---b--lhr|t-lhhhr---\nb---------|v---------\n--------lr|b-lr------", 
			"-lr----lr-|----lhhr--\n----------|-lhr----t-\n---t--lhr-|t-------b-\n---b------|b-lhhr----\nt-t---lhhr|----------\nb-v--t----|lr----lhr-\n--v--v---t|----lr----\n--b--b---v|--------t-\n---------b|lhhhr---v-\n--lhhhr---|--------b-", 
			"---t----t-|-lr-------\n---v----b-|t------lr-\n---b------|b--lhhr---\nlr--lhhhr-|---------t\n----------|----lr---v\n-t--lr-t--|--t------v\n-v-t---v--|t-v------v\n-b-v-t-v--|v-b--lhr-b\n---v-v-b--|b---------\nlr-b-b----|----lhhr--", 
			"-lhhhr----|lr--------\n----------|---lhr----\nt-t--lhr--|lhr----lr-\nv-v------t|----------\nv-v------b|-lhhhr-lr-\nb-b-lr-t--|----------\n-------b-t|t-t-------\n-t--t----v|v-v-lhhr--\n-b--v----b|v-b-----lr\n----b-----|b---------", 
			"------lhhr|------lhhr\n-lhr------|----------\n----t-t-t-|---lr-----\n----v-b-v-|-----lhhhr\n-t--b---v-|lr--------\n-b----t-b-|---t--lhhr\n------b---|t--v------\nlhhhr-----|b--b------\n----------|-----lhr--\n---lr--lhr|--lhr---lr", 
			"--------t-|--lr--lhhr\n----lhr-v-|----------\nlhr-----b-|-lhhhr-t--\n----------|-------b--\nlr-t-lr---|---lhhr---\n---v------|-------lhr\n-t-v-lhhr-|----------\n-v-v------|----lhr--t\n-v-b-t-lr-|-t-------v\n-b---b----|-b----lr-b", 
			"lr--------|---------t\n--lhr-t---|---------b\n------b-t-|--lhr-----\n-t------v-|-t---t--lr\n-b--t---b-|-v-t-v-t--\nt---b-----|-v-v-v-v--\nv-t----lhr|-v-b-b-b--\nv-v-------|-b--------\nv-v---lhhr|----lhhr-t\nb-b-------|--lr-----b", 
			"--lhr-lr--|----------\n----------|lhhr---t--\n---lhhr--t|----t--v--\n--t------v|--t-b--b--\n--b------b|--v--t----\nt--t-lhr--|t-v--b--lr\nv--b------|v-v----t--\nv-------lr|v-b----v-t\nb---------|b---lr-b-v\n----lhhhr-|---------b", 
			"--lhhhr---|t--lhhr---\n--------lr|v------t-t\n----lhhr--|b------v-b\n--lr------|-lhr-t-v--\n-----lr---|-----b-b--\n--lhr-----|t---t-----\n-------lhr|v---b-lhr-\n--t-------|v---------\n--v-t-lhhr|v---------\n--b-b-----|b----lr---", 
			"-t----t---|----------\n-v----b---|-----lhhr-\n-b--------|t---------\n---t-t---t|b-t-t--lhr\n---v-b---v|--b-v-----\n---v---t-v|----v-lhhr\n-t-b-t-v-v|--t-v-----\n-v---v-b-b|t-v-b--t--\n-b---v--t-|b-b--t-v--\n--lr-b--b-|-----b-b--", 
			"lhhhr-----|--lhhhr---\n-----t----|----------\n-t---v--lr|-t-t-lhr--\n-b-t-v----|-b-v------\n---v-b----|t--v-t---t\n---b--t---|b--b-v---b\nt---t-v-t-|--t--b----\nv---v-b-b-|--v-----t-\nb---v----t|--v-lr--v-\n----b----b|--b-----b-", 
			"-lhhhr----|t---------\n--------lr|v-----lr--\n-lhhr-----|v---t-----\n-----t-t--|b---v-t-t-\n--lr-v-b-t|-lr-b-v-b-\n-----v---v|------b---\n-----b---b|--lhhr-lhr\nlr--t-t---|----------\n----v-v---|--lhhhr-lr\n----b-b---|----------", 
			"----------|----------\nlr--t--t--|-t--lhr---\n----b--v-t|-v-t-----t\n--t----b-v|-b-v---t-v\n--v---t--b|---v-t-b-v\n--v-t-v---|-t-v-v---b\n--v-v-v-t-|-v-b-b----\n--b-b-b-b-|-v-----lr-\n-------t--|-b-t------\n-lhhr--b--|---b---lr-", 
			"-lr----lr-|t---------\n---lhr----|v----lhhr-\n----------|b---------\n-lhhhr-lr-|-lr--t----\n----------|-----v--t-\n--t---lhhr|--t--b--b-\n--v-------|t-v---t---\n--b----lhr|v-v-t-b---\n---t------|v-v-b--lhr\n---b-lhhr-|b-b-------", 
			"---t------|-----t----\n---b-lhhr-|-t---b-lhr\nt---t-----|-v-t------\nv---b--lr-|-b-v--lr-t\nb-----t--t|---v-----v\n--t---v--b|---b-----v\n--v-t-v---|----lr-t-v\n--v-v-b---|-------v-b\n--v-b-----|t--lhr-v--\n--b----lhr|b------b--", 
			"lhr---lhhr|-lhhr---t-\n----------|------t-v-\n-----lhr--|---lr-v-v-\n-lhr------|-t----b-v-\n------t--t|-v-t----b-\n----t-b--b|-b-v--t---\n-t--v--lr-|---v--v---\n-v--v-t---|---b--b---\n-v--v-b---|lr--------\n-b--b-----|---lr-lr--", 
			"-------t--|----lhr---\n----t--v--|--t----t--\n----b--b--|--b----b--\n-----t--lr|-t--lhr-t-\nlhhr-b----|-v-t----b-\n-------t--|-v-v--lr--\nlhr----v--|-v-b------\n---lhr-v--|-b---lhhr-\n-------b-t|----------\n--lhhhr--b|-----lhhr-", 
			"-----lhr--|----------\nlr------lr|-lhhr-----\n---lhhr---|-----t-t-t\n-------t--|---t-b-v-v\n-lhhr--v--|-t-v---v-b\nt------v--|-b-b---b--\nv------v--|----lhr---\nb-t-t--b-t|----------\n--v-b----b|--lr-lhhhr\n--b-------|lr--------", 
			"-lr----lhr|-----t----\n---lhr----|-----b--lr\n----------|t---------\n--lhhr--lr|v----lhhhr\n----------|v-t-t-----\n--------t-|b-b-b-----\n-lr-----v-|-------lhr\n---lhhr-b-|-t--t-----\n----------|-v--v-----\n-lhhhr--lr|-b--b-lhhr", 
			"--lhhhr---|----------\n-t--------|----------\n-b--t-lhhr|--lr--lhhr\n----b-----|----------\n-lhr-lr---|-t-lhr-t-t\nt---------|-v-----b-v\nb----lhr--|-b-lhhr--v\n-t--------|t------t-v\n-v-lhhr---|v---t--b-b\n-b--------|b---b-----", 
			"-----lhr--|-------lhr\n----------|t----t----\n------lr--|b----b----\n-lhhr-----|-lhhr-----\n-----lhhr-|------t-t-\n-t-lr-----|t-----v-v-\n-v---lhr--|v-t---b-b-\n-v--t----t|v-v-t----t\n-v--v-t--b|v-v-b----b\n-b--b-b---|b-b-------", 
			"--t---lhr-|-t------lr\n--b-t-----|-b--------\n-t--b---t-|-------lhr\n-v---t--v-|lhr--t----\n-b---b--v-|-----v----\n--lhr-t-v-|lhhr-b----\nlr----v-b-|------t---\n------v---|lhhr--b-lr\n-lhhr-b---|----------\n----------|-lhhhr----", 
			"--lr-lr---|-------lhr\n-------t--|---lhr----\n-------v--|------t-lr\n---lhr-v--|lhhr--b---\n-t-----b--|----------\n-b--lhr---|--t------t\n---------t|--v----t-v\nt--lhr---v|--b----v-v\nb--------v|---lr--v-v\n--lhhhr--b|-lr----b-b", 
			"---t------|-----lhhr-\n---b--lhhr|-t-t------\n-----t----|-v-b---t--\nt-t--b----|-v---t-b--\nv-v------t|-v---b----\nv-b---lr-v|-b-----lhr\nv----t---b|-----t----\nb--t-v--t-|-lhr-b-t--\n---b-v--v-|-------v--\n-----b--b-|--lhhr-b--", 
			"-lhhr-t---|------t---\n------v--t|--lr--v---\nlhr---v--v|------b-lr\n---t--v--b|-t--------\n---b--b---|-v-t-lhr--\n-lr-------|-v-b------\n-------t--|-b--t-----\n---lhr-b--|t---b-lhhr\n-lr-------|v---------\n----lhhr--|b----lhhhr", 
			"----t-----|lhr--lr---\n----b---lr|--------t-\n-lhr------|---t--t-v-\n----t-t---|---b--b-b-\nt---v-v--t|--t-------\nv---b-v--b|--b--lhhhr\nv-t---b---|---t------\nb-v-----t-|-t-v-lhhr-\n--b-----b-|-v-v------\n---lhhhr--|-b-b------", 
			"-t------lr|lhhhr-----\n-v---t----|-------lhr\n-v---v----|t-lhr-----\n-b---v-t-t|v-----lr--\n-----b-v-b|b--t------\n----t--b--|---v--lr--\n--t-v---lr|-t-v------\n--v-b-lr--|-v-b----lr\n--b-------|-v---lr---\n-----lhhhr|-b--------", 
			"--t--lhhhr|---t---lr-\n--b-------|-t-b------\n---lhr----|-v--lhr---\nlhr-----lr|-v-----lhr\n-----lhr--|-b-lhr----\n--lr------|t-----lr--\n-t------t-|v---t-----\n-v------b-|v---b-----\n-v-lhhr---|v---------\n-b--------|b----lhhr-", 
			"--lr------|----lr----\n------lhhr|-t----lhhr\n---t------|-v--t-----\n-t-b----t-|-b--b-t---\n-v--lhr-b-|------v-t-\n-v--------|lr----v-v-\n-v-t--lhhr|---lr-b-b-\n-b-b------|----------\n-----lhr--|------lhr-\n-lhr------|lhhhr-----", 
			"----------|--lr---t--\n--lhhhr---|-------b--\n-------lr-|------t---\nlhr------t|-lhhr-v-lr\n---lhhr--v|------v---\n---------b|------b-t-\nt-t-------|lhr-----v-\nb-v--t-lr-|---lhr--b-\n--v--b----|----------\n--b---lhr-|-lr-lhhhr-", 
			"----------|--lr-lr---\n---lhr-t-t|----t--lhr\n-------v-v|----v-----\nt------v-v|----b-----\nb--lhr-b-b|t-----lhr-\n-t--------|v--------t\n-b--lr----|v-t----t-v\n---t--lhr-|v-b----v-v\n---b------|b--lr--v-b\n----lhhhr-|-------b--", 
			"-----lr---|--lhr-----\n----------|lr------lr\nt-lhr---t-|------t---\nv----lr-v-|----t-v-t-\nv--t----b-|-t--v-v-v-\nv--v-----t|-b--v-b-b-\nb--b---t-v|--t-b-----\n-lr----v-v|--v--lhhhr\n----lr-v-b|--b-------\n-------b--|lr--------", 
			"t---------|---t------\nv--t-t----|-t-v-t----\nv--b-b-lhr|-b-v-b-t--\nv---------|---b---b--\nb----t-lr-|----------\n-----b----|--lhr----t\n-t--t-----|-t---lhr-v\n-v--v-t-t-|-v-t-----b\n-v--v-v-v-|-v-b------\n-b--b-b-b-|-b--lhhhr-", 
			"---lr-lr--|t---------\n---------t|b-t-------\n--t-t----b|--v----lr-\n--v-b-----|t-v--t----\nt-v------t|v-b--v-lr-\nv-b--lhr-v|v--t-b----\nv--------b|v--v----t-\nv-lhr-----|b--b-t--v-\nb----lhhr-|-----b--b-\n----------|-lhhr-----", 
			"-----t----|lhr-------\n--lr-v----|------lhhr\nt----b----|--lhhr----\nv---t-----|-------t-t\nv---v-lhhr|-lhhhr-v-b\nb---b-----|-------b--\n-lr-------|lr------t-\n------t-lr|--lhr---b-\n------b---|----------\n-lhhhr-lhr|--lr------", 
			"-lhr--lr--|lhhhr-----\n----------|------lhr-\n---t-lhr--|----------\n-t-v------|t--lr-lr--\n-b-b-lr---|v---------\n----------|v-lhr-----\n--lhhhr-t-|b------t-t\n--------v-|-lhr---b-v\nlr-lhhr-v-|------t--v\n--------b-|------b--b", 
			"----------|lr--------\nlhhr------|--lhhr----\n----lr-lhr|----------\nlhr-------|----lr-t-t\n---t-lr---|lr-----v-v\n-t-v------|-----t-v-v\n-b-v------|-t---b-b-v\n---v-lhhr-|-v--t----b\n---b-----t|-b--v-lhr-\n----lhr--b|----b-----", 
			"-----lhr--|------lhhr\nt---------|-lhr------\nb-lhhhr-lr|--------lr\n-t--------|------lr--\n-b-----t-t|---------t\n-------v-v|---lhr-t-b\n--lhhr-b-v|-------v--\n---------b|lhhhr--v--\n--lhr-lr--|-------b-t\n----------|-lhr-----b", 
			"----------|t-----lhhr\n-----t---t|v---------\n-lhr-v-t-v|v-----t---\n-----v-b-v|b-----b---\n-t-t-b---v|-------lhr\n-v-b-----b|lhr-t-----\n-b--lr-t--|----b-----\n-------b--|-lr--lhr--\nlhhr------|--------lr\n----lhr---|--lhhhr---", 
			"----------|----------\n-lr----lr-|-lhhhr---t\n---lhr----|-------t-b\n------t--t|--lr---v--\n-t----v--b|----lr-b-t\n-v--t-v-t-|lhr------v\n-b--v-v-v-|------t--v\n--t-v-b-v-|----t-v--b\nt-v-b---b-|--t-v-v---\nb-b-------|--b-b-b---", 
			"-lr-------|----------\nt--lr-t---|-lhhr-----\nv-----v-t-|-------lhr\nv-----b-b-|-lhhhr----\nv-lhr-----|-------lr-\nb---------|-t--------\n-lhr--lhhr|-b--lr--lr\n----------|t-t-------\n---t--lhhr|v-v-------\n---b------|b-b--lhhr-", 
			"t--t-lr---|----------\nv--v----t-|---lhhr---\nv--v--t-v-|t---------\nb--b--v-b-|v--lr--lhr\n------v---|v---------\n----t-v---|b-lhr-lhr-\n-t--v-b---|-----t----\n-v--b--lr-|--lr-b----\n-b-t--t---|-t--------\n---b--b---|-b--lhhhr-"
			};

	
	
	String[] LEVEL_NOT_CORRECT_ARRAY =  {
			//wrong width/height
			"--lr--lhhr|------lhhr\n----------|--lhr-----\n----------|--------lr\n---lhr----|-lhr------\n-t------lr|-------t--\n-v-lhr----|-t-----b--\n-b------t-|-b-lr-----\n---lhhr-b-|-----lhhr-\n----------|lhhhr-----", 
			"-lhhhr----|----------\n------t---|----lhr-lr\nlhr-t-v---|--lr------\n----b-b---|----t-----\n--t------t|----v-lr--\nt-b-----v|lhr-b-----\nv-----lr-v|---t-lhhr-\nb--------b|---v------\n-lhhr---t-|-t-v-lhhhr\n--------b-|-b-b------", 
			"------lhhr|------lhhr\n---lr-----|-t-lhr----\n------lhr-|-v--------\n-lhr------|-v--lhr-lr\n-----lr--|-b--------\nt-------t-|-----lhr-t\nb-lhr---v-|---------b\n-----lr-v-|------lr--\n-lhhr---v-|-lhhhr--lr\n--------b-|----------", 
			"t--t-lr---|----------\nv--v----t-|---lhhr---\nv--v--t-v-|t--------\nb--b--v-b-|v--lr--lhr\n------v---|v---------\n----t-v---|b-lhr-lhr-\n-t--v-b---|-----t----\n-v--b--lr-|--lr-b----\n-b-t--t---|-t--------\n---b--b---|-b--lhhhr-",
			"-lhr--t---|----------\n------b---|--lhhhr---\nlr------t-|-t------t-\n---lhhr-b-|-b----t-v-\n-t-------|t---t-v-b-\n-v----t---|b-t-v-b---\n-v--t-v-t-|--b-v-----\n-v--b-b-v-|----b---t-\n-b------v-|-----lr-v-\n---lhr--b-|lhhr----b-", 
			"lhhhr----|---------\n------t--|---lhr-lr\nlhr-t-v--|-lr------\n----b-b--|---t-----\n--t-----t|---v-lr--\nt-b-----v|----b--lhr\nv----lr-v|--t-lhhr-\nb-------b|---v-----\n-lhhr---t-|t-v-lhhhr\n-------b-|-b-b-----", 
			" \n-----lhhr|-----lhhr\n---lr----|t-lhr----\n------lhr|v--------\n-lhr-----|v--lhr-lr\n------lr-|b--------\nt-------t|----lhr-t\nb-lhr---v-|---------b\n-----lr-v-|------lr--\n-lhhr---v-|-lhhhr--lr\n--------b-|----------", 

			//contains whitespace(s) and/or illegal newline(s)
			" -lhr--t---|----------\n------b---|--lhhhr---\nlr------t-|-t------t-\n---lhhr-b-|-b----t-v-\n-t--------|t---t-v-b-\n-v----t---|b-t-v-b---\n-v--t-v-t-|--b-v-----\n-v--b-b-v-|----b---t-\n-b------v-|-----lr-v-\n---lhr--b-|lhhr----b-", 
			"t---lhr---|----------\n v---------|-------lr-\nv-t--lr--t|-t--------\nv-v------b|-v-lhr--lr\nb-b--lhhr-|-b--------\n---t------|t-t-t-----\n---b------|v-v-v-t---\n-t----lhhr|v-v-b-b---\n-v-lr-----|b-b-----lr\n-b--------|---lhhhr--", 
			"t--lr--lhr|-----lr-lr\nv----lr---|-lhhr--t--\nv-t----lhr| -------b--\nb-v-------|--t-lhr---\n--v-t-----|--b------t\n--b-v---lr|-t--t--t-v\n----b-----|-v--v--v-v\n----------|-v--b--b-v\nlhhhr--t--|-b-------b\n-------b--|----------", 
			"-lhr--t---|----------\n------b---|--lhhhr---\nlr------t-|-t------t-\n---lhhr-b-|-b----t-v-\n-t--------|t---t-v-b-\n -v----t---|b-t-v-b---\n-v--t-v-t-|--b-v-----\n-v--b-b-v-|----b---t-\n-b------v-|-----lr-v-\n---lhr--b-|lhhr----b-", 
			"t---lhr---|----------\nv---------|-------lr-\nv-t--lr--t|-t--------\nv-v------b|-v-lhr--lr\nb-b--lhhr-|-b--------\n---t------|t-t-t-----\n---b------|v-v-v-t---\n-t----lhhr|v-v-b-b---\n-v-lr-----|b-b-----lr\n\n-b--------|---lhhhr--", 
			"t--lr--lhr|-----lr-lr\nv----lr---|-lhhr--t--\nv-t----lhr|-------b--\nb-v-------|--t-lhr---\n--v-t-----|--b------t\n--b-v---lr|-t--t--t-v\n----b-----|-v--v--v-v\n----------|-v--b--b-v \nlhhhr--t--|-b-------b\n-------b--|----------\n", 
			"--t--lhr--|--------lr\n--b------t|-lhhr-----\n-------t-v|--------t-\n----t--v-v|-lhr--t-b-\n-t--b--v-b|------v--t\n-v-t---v--|---lr-v--v\n-b-v---b--|------b--v\n---b------|-t-------v\n------t-lr|-b-lhr---b\n-lhhr-b---|------lhr- ", 
			
			//misplaced border
			"----------|----------\n--lr------|-lhr------\n-t-----lhr|----------\n-v-lr-----|--lhr----t\n-b---lhhr-|-------t-b\n----------|-lhhhr-v--\n---lhhr-lr|-------v-t\n-lr-------|-lhr---b-b\n------lhr-|----lr----\n-lhhhr-----lr---lhhr", 
			"---lhhr---|-------lhr\n-t--------|-t--------\n-b---lhhr-|-v----lr--\n---lr-----|-v---t---t\n-------lr-|-b---v-t-v\n-lhhhr----|---t-v-b-v\n--------t-|-t-v-v---b\nt--lhr--v-|-b-b-b--t-\nv-------b-|--------v-\nb-----lr--||---lr--b-",
			"---t--t---|----------\n-t-v--v---|-----lhr--\n-b-b--b-lr|----------\n----------|lr----lhr-\n-lr--lhhr-|--lhhr----\n----t-----|t------t-t\n-t--v-lhhr|v--t---b-v\n-v--v-----|b--b-----v\n-b--v--t--|------lr-v\n----b--b--||--lhhr---b",
			
			//these levels contain illegal characters:
			"--t-------|-----lhhr-\nt-b---lr--|--lr------\nv---------|----------\nb-lhhr-lhr|t------lr-\n-t--------|v--t--t---\n-v-t---t--|b--b--v-lr\n-b-v---v--|-t--t-v---\n---v---v--|-v--v-v-t-\n---b---v--|-v--b-b-v-\nlr--lr-b--|-b--a---b-", 
			"------lhhr|-----lhhr-\n-t---t----|-t--------\n-b---b--t-|-v-lr-lhr-\n--lhr-t-v-|-v-------t\n------v-v-|-b-lr----v\n-lhhr-b-v-|-----t---b\n--------b-|-----v-lr-\nt---t-----|--t--v----\nb---v-----|--b--v-lhr\n----b---lr|--c--b----", 
			"--lhr-----|-lhr--lhr-\n-----lr--t|----------\n---------v|-lhhr---lr\nt-t------b|-------t--\nv-v-t-----|-t-----v--\nv-v-v-lr--|-v-t---v--\nv-b-b-----|-b-v-t-b-t\nb------lr-|---v-b---b\n-t-lhhr---|---v---d--\n-b--------|---b----lr", 
			"t----lhr--|----lr-t--\nb--lr-----|-------b--\n-t--------|--lhhr--t-\n-b----lhhr|------t-v-\n----------|t-lhr-b-b-\n-t---d---t|v---------\n-v--lhhr-v|b--lhhhr--\n-b-------v|--t-------\n-------t-v|--b--lhhr-\n-lhr---b-b|----------", 
			"---lhhr---|---lhhhr--\n----------|-lr-----t-\n-t--lr--lr|--------v-\n-b-t------|---lhhr-v-\nt--v-e---t|--------b-\nv--b---t-v|-lr-lhr---\nv------v-v|t--t------\nv------b-b|v--v--t-t-\nb---------|b--b--b-b-\n--lhr--lr-|----------", 
			"---lr--lhr|----------\nlhr--lr---|lhr-lhhhr-\n----------|---t------\n---------t|-t-v---lhr\n-lhhr-t--b|-b-v--f---\n------b---|---b--t-t-\n-lhhr-----|-lr---v-v-\n-----lhr--|---lr-v-b-\n----------|------b--t\n---lhhhr--|---------b", 
			"----lhhr-t|--t-------\n---------b|--v----lhr\n---lhr----|--v-t-----\n-lr-----t-|--b-b---t-\nt---1---b-|t--t--t-b-\nv--t-lhr--|v--v--b--t\nv--v------|b--b-----v\nb--v-t----|---------v\n---v-v-lr-|--lhhr---v\n---b-b----|------lr-b", 
			"----t---lr|---lhr----\n-t--v--t--|--t-------\n-v--v--v--|--v----t-t\n-v--b--v-t|--b-t--v-v\n-b--5--v-b|----v--v-b\n----t--b--|----v--b--\n-t--b----t|-lr-b-----\n-v-t-----v|t-------lr\n-b-v-----b|b--lr-----\n---b--lr--|-----lhhhr", 
			"------t-lr|----t---t-\nt--t--v---|-t--v---b-\nv--v--b---|-v--b--t-t\nv--v------|-b-t-³-v-v\nv--b--lhhr|---b---v-v\nb---------|-------v-b\n--lhr----t|t------b--\n-----lr--b|b--lhhr---\n--t-------|--t-------\n--b-lhr---|--b---lhr-", 
			"-t--------|-----lhhr-\n-v-lhhr-t-|-t--------\n-v------v-|-v---lhr--\n-v--t---b-|-v-lr---t-\n-b-²v-lr--|-v------b-\n----b-----|-b--------\nt--------t|--t------t\nb-lhhr---b|--v---lr-v\n-------lr-|--v-lr---b\n----lhr---|--b---lhr-", 
			"----------|-lhr------\n-lr-t-----|t----lhhhr\n----v-lhr-|v---------\n-t--v-----|v----lr---\n-v--b--lhr|b----^----\n-v--------|----t---lr\n-b------t-|-lr-v-----\n--lhhhr-b-|----b--lhr\nt---------|----------\nb-lr--lhr-|--lhhr--lr", 
			"-----lhr--|t--lhhhr--\nt---t----t|v---------\nb---b----v|b-lhr-----\n--t--lhr-v|------lhhr\n--b------b|lr-t---°--\n-t--------|---b------\n-v--lhhhr-|lhr-lhhr--\n-v-------t|----------\n-b---t---v|---t------\n-----b---b|---b----lr", 
			"-----lr---|----------\n--t-t-----|------lhr-\n--v-b-lhr-|----lr----\n--b-------|-lr----t-t\n---lr-----|----lr-v-v\nt---%-----|--t----b-v\nv-----lr--|--v-t----b\nv-lhhr----|t-v-b-----\nv---------|v-b--lhhhr\nb-lhhr-lhr|b---------", 
			"-lr---lr--|------lr--\n---------t|--lr-t----\n--lhhr---b|-----v-lr-\nt------t--|-----v----\nv----&-v--|-----v-t--\nb---t--b--|lhhr-b-v--\n----v-t-t-|-------b--\n----v-v-v-|lhr---t---\n-t--v-v-b-|------b---\n-b--b-b---|-lhhr--lhr", 
			"-t-lr-----|------lhhr\n-v-------t|-lhhhr----\n-v--lhhr-b|-------t-t\n-b--------|-t-{-t-b-v\n-------t--|-b---v---b\n-t-lr--b--|--lr-v----\n-v--------|-----b----\n-v-t-t-lhr|--------t-\n-v-v-v----|-lhr----b-\n-b-b-b----|-----lhr--", 
			"----------|--lhr--lr-\n-lr--lhhhr|lr--------\n----------|------t---\nt-----t---|---<>--b--t\nv-t-t-v-lr|-lhhhr---v\nv-v-b-v---|------t--b\nb-b---b-t-|------v-t-\n--------v-|lhhr--b-v-\nlhr---t-b-|----t---v-\n------b---|----b---b-", 
	};
	
	
	
	
	BattleshipTestAdapterMinimal adapter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		adapter = new BattleshipTestAdapterMinimal();
	}

	@Test
	public void testLoadCorrectLevelsFromStringArray() {
		for (int i = 0; i < LEVEL_CORRECT_ARRAY.length; i++) {
			adapter.createGameUsingLevelString(LEVEL_CORRECT_ARRAY[i]);
			assertTrue("A correct level was identified wrongly as not correct.", adapter.isValidLevel());
		}
	}

	@Test
	public void testLoadNotCorrectLevelsFromStringArray() {
		for (int i = 0; i < LEVEL_NOT_CORRECT_ARRAY.length; i++) {
			adapter.createGameUsingLevelString(LEVEL_NOT_CORRECT_ARRAY[i]);
			assertFalse("Level with index "+i+" of LoadLevelBigTest.LEVEL_NOT_CORRECT_ARRAY was identified worngly as correct.\n Please see java-comments for more information about the (not) correctness of the level. \n the level is:\n"+LEVEL_NOT_CORRECT_ARRAY[i], adapter.isValidLevel());
		}
	}

}
