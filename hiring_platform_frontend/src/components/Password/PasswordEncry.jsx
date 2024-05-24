import React, { useState } from 'react'
import { encrypt } from "n-krypta"
import InputTag from '../InputTag/InputTag';
import Button from '../Button';
function PasswordEncry() {
  const [password, setPassword] = useState();
  const seckey = 'key'
  const change = () => {
    const newpass = encrypt(password, seckey)
  }
  return (
    <div>
      <InputTag type="password" onChange={(data) => setPassword(data.target.value)} />
      <Button onClick={change}>encrypt/</Button>
    </div>
  )
}

export default PasswordEncry
