import { LocalStorageAdapter } from './local-storage.adapter';
import { User } from '../../../domain/entities/user.entity';

describe('LocalStorageAdapter', () => {
  let adapter: LocalStorageAdapter;
  let store: Record<string, string>;

  beforeEach(() => {
    adapter = new LocalStorageAdapter();
    store = {};
    spyOn(localStorage, 'getItem').and.callFake((key: string) => store[key] ?? null);
    spyOn(localStorage, 'setItem').and.callFake((key: string, value: string) => {
      store[key] = value;
    });
    spyOn(localStorage, 'removeItem').and.callFake((key: string) => {
      delete store[key];
    });
  });

  it('saves and reads user data and token', () => {
    const user: User = { fullName: 'Test User', email: 'test@example.com', role: 'EMPLOYEE' };

    adapter.saveUser(user, 'token-123');

    expect(localStorage.setItem).toHaveBeenCalledWith('token', 'token-123');
    expect(localStorage.setItem).toHaveBeenCalledWith('foodtrack-user', JSON.stringify(user));
    expect(adapter.getToken()).toBe('token-123');
    expect(adapter.getUser()).toEqual(user);
    expect(adapter.isAuthenticated()).toBeTrue();
  });

  it('returns null and unauthenticated when nothing is stored', () => {
    expect(adapter.getUser()).toBeNull();
    expect(adapter.getToken()).toBeNull();
    expect(adapter.isAuthenticated()).toBeFalse();
  });

  it('clears stored session data on logout', () => {
    store['token'] = 'token-123';
    store['foodtrack-user'] = JSON.stringify({ fullName: 'Test User', email: 'test@example.com', role: 'EMPLOYEE' });

    adapter.logout();

    expect(localStorage.removeItem).toHaveBeenCalledWith('token');
    expect(localStorage.removeItem).toHaveBeenCalledWith('foodtrack-user');
    expect(adapter.getToken()).toBeNull();
    expect(adapter.getUser()).toBeNull();
  });
});
