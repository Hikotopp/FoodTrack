import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../../../environments/environment';
import { AuthHttpAdapter } from './auth-http.adapter';

describe('AuthHttpAdapter', () => {
  let adapter: AuthHttpAdapter;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthHttpAdapter]
    });

    adapter = TestBed.inject(AuthHttpAdapter);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('posts credentials to the login endpoint', (done) => {
    const response = {
      token: 'token',
      fullName: 'Test User',
      email: 'test@example.com',
      role: 'EMPLOYEE' as const
    };

    adapter.login('test@example.com', 'Password123!').subscribe((result) => {
      expect(result).toEqual(response);
      done();
    });

    const request = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({ email: 'test@example.com', password: 'Password123!' });
    request.flush(response);
  });

  it('posts account data to the register endpoint', (done) => {
    const response = {
      token: 'token',
      fullName: 'New User',
      email: 'new@example.com',
      role: 'EMPLOYEE' as const
    };

    adapter.register('New User', 'new@example.com', 'Password123!').subscribe((result) => {
      expect(result).toEqual(response);
      done();
    });

    const request = httpMock.expectOne(`${environment.apiUrl}/auth/register`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      fullName: 'New User',
      email: 'new@example.com',
      password: 'Password123!'
    });
    request.flush(response);
  });
});
