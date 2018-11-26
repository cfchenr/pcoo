/**
 * A concurrent library based on Design-by-Contract.
 *
 * <p>This package implements a  <a href="http://en.wikipedia.org/wiki/Design_by_contract">Design-by-Contract</a>
 * based concurrency library for programming language Java.
 * One of its mains features is its simplification of the usage of native Java concurrency constructs
 * (<tt>wait</tt>, <tt>notify</tt>, <tt>notifyAll</tt>, <tt>join</tt>, etc.) by replacing the annoying
 * <tt>InterruptedException</tt> checked exception with an equivalent
 * {@link pt.ua.concurrent.ThreadInterruptedException ThreadInterruptedException} unchecked exception
 * (easing the development of concurrent Java code, and preventing a serious source of
 * Java bugs due to the erroneous algorithm of ignoring exceptions). 
 *
 * <p>This library is also the base of a new approach for object-oriented concurrent programming in which
 * both models of thread communication -- shared object and message passing -- are supported.
 * In the shared object model it is possible to fully synchronize a shared object with all the three aspects
 * required -- internal, conditional and external -- without forcing the internal synchronization of the
 * object to a monitor (mutual exclusion).
 * In fact, one may safely use an internal synchronization scheme based on CoW (copy-on-write) or transactions,
 * and still be able to externally synchronize the shared object.
 *
 * <p> This package contains the following groups of modules:
 *
 * <p><b>Base library modules:</b></p>
 *
 * {@link pt.ua.concurrent.InterruptibleAwaitingThreads InterruptibleAwaitingThreads}: Java's interface to extend support for Interruptible Awaiting Threads<br>
 * {@link pt.ua.concurrent.CThread CThread}: a replacement of Thread<br>
 * {@link pt.ua.concurrent.CRunnable CRunnable}: a replacement of Runnable (to correctly handle exceptions and to register start time)<br>
 * {@link pt.ua.concurrent.ThreadInterruptedException ThreadInterruptedException}: InterruptedException unchecked exception replacement<br>
 * {@link pt.ua.concurrent.CObject CObject}: a replacement of Object for Sync and/or SynCV purposes<br>
 * {@link pt.ua.concurrent.WrapRunnable WrapRunnable}: transforms a Runnable into a CRunnable<br>
 *
 * <p><b>Utility modules:</b></p>
 *
 * {@link pt.ua.concurrent.Console Console}: output to a console supporting colors<br>
 * {@link pt.ua.concurrent.Metronome Metronome}: implementation of an active (thread) metronome class, able to periodically synchronize any number of threads<br>
 *
 * <p><b>Elementary synchronization modules:</b></p>
 *
 * {@link pt.ua.concurrent.Signal Signal}: a simple signal abstraction module<br>
 * {@link pt.ua.concurrent.TransientSignal TransientSignal}: a transient signal module<br>
 * {@link pt.ua.concurrent.PersistentSignal PersistentSignal}: a persistent signal module<br>
 * {@link pt.ua.concurrent.Event Event}: a simple module supporting two valued events<br>
 * {@link pt.ua.concurrent.Semaphore Semaphore}: a standard general semaphore<br>
 * {@link pt.ua.concurrent.BoundedSemaphore BoundedSemaphore}: a semaphore bounded by a maximum value<br>
 * {@link pt.ua.concurrent.BinarySemaphore BinarySemaphore}: a binary semaphore<br>
 * {@link pt.ua.concurrent.Channel Channel}: a simple module support a (generic) communication channel between two threads<br>
 * {@link pt.ua.concurrent.Exchanger Exchanger}: a simple module support a (generic) exchanger (bidirectional) communication channel between two threads<br>
 *
 * <p><b>Locking modules:</b></p>
 *
 * {@link pt.ua.concurrent.Sync Sync}: Java's interface for locks<br>
 * {@link pt.ua.concurrent.SyncCV SyncCV}: Java's interface for condition variables<br>
 * {@link pt.ua.concurrent.Mutex Mutex}: a POSIX-threads alike mutual exclusion synchronization class<br>
 * {@link pt.ua.concurrent.MutexCV MutexCV}: a POSIX-threads alike mutex condition variable class<br>
 * {@link pt.ua.concurrent.RWEx RWEx}: a readers-writer exclusion synchronization class<br>
 * {@link pt.ua.concurrent.RWExCV RWExCV}: a readers-writer exclusion condition variable class<br>
 * {@link pt.ua.concurrent.GroupMutex GroupMutex}: a group exclusion synchronization class<br>
 * {@link pt.ua.concurrent.GroupMutexCV GroupMutexCV}: a group exclusion condition variable class<br>
 * {@link pt.ua.concurrent.SyncState SyncState}: an internal module (to ease the implementaion of getStateAndUnlock/recoverState services)<br>
 * {@link pt.ua.concurrent.GroupMutexComposedCV GroupMutexComposedCV}: composed group mutex CV to allow a CV applicable to a GroupMutex and one of any element of an array of Sync objects<br>
 * {@link pt.ua.concurrent.MutexSetCV MutexSetCV}: a Mutex set condition variable class<br>
 * {@link pt.ua.concurrent.AtomicLockingMutexSet AtomicLockingMutexSet}: atomic locking operation on an unordered set of mutexes<br>
 *
 * <p><b>Barrier modules:</b></p>
 *
 * {@link pt.ua.concurrent.Barrier Barrier}: a thread barrier interface<br>
 * {@link pt.ua.concurrent.InternalBarrier InternalBarrier}: an internally triggered thread barrier abstract class<br>
 * {@link pt.ua.concurrent.ExternalBarrier ExternalBarrier}: an externally triggered thread barrier abstract class<br>
 * {@link pt.ua.concurrent.FixedBarrier FixedBarrier}: a fixed sized thread barrier class<br>
 * {@link pt.ua.concurrent.DynamicBarrier DynamicBarrier}: a dynamically sized thread barrier class<br>
 * {@link pt.ua.concurrent.TriggeredBarrier TriggeredBarrier}: an externally triggered thread barrier class<br>
 *
 * <p><b>Message passing modules:</b></p>
 *
 * {@link pt.ua.concurrent.Actor Actor}: an actor communication class<br>
 * {@link pt.ua.concurrent.Future Future}: actor's future results class<br>
 *
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.9
 *
 */
package pt.ua.concurrent;

